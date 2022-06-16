package com.jmtc.tracegoods.antchain;

import com.alipay.mychain.sdk.api.MychainClient;
import com.alipay.mychain.sdk.api.env.ClientEnv;
import com.alipay.mychain.sdk.api.env.ISslOption;
import com.alipay.mychain.sdk.api.env.SignerOption;
import com.alipay.mychain.sdk.api.env.SslBytesOption;
import com.alipay.mychain.sdk.api.logging.AbstractLoggerFactory;
import com.alipay.mychain.sdk.api.logging.ILogger;
import com.alipay.mychain.sdk.api.utils.Utils;
import com.alipay.mychain.sdk.common.VMTypeEnum;
import com.alipay.mychain.sdk.crypto.MyCrypto;
import com.alipay.mychain.sdk.crypto.keyoperator.Pkcs8KeyOperator;
import com.alipay.mychain.sdk.crypto.keypair.Keypair;
import com.alipay.mychain.sdk.crypto.signer.SignerBase;
import com.alipay.mychain.sdk.domain.account.Identity;
import com.alipay.mychain.sdk.errorcode.ErrorCode;
import com.alipay.mychain.sdk.message.transaction.AbstractTransactionRequest;
import com.alipay.mychain.sdk.message.transaction.TransactionReceiptResponse;
import com.alipay.mychain.sdk.message.transaction.contract.CallContractRequest;
import com.alipay.mychain.sdk.type.BaseFixedSizeUnsignedInteger;
import com.alipay.mychain.sdk.utils.ByteUtils;
import com.alipay.mychain.sdk.utils.IOUtil;
import com.alipay.mychain.sdk.utils.RandomUtil;
import com.alipay.mychain.sdk.vm.EVMOutput;
import com.alipay.mychain.sdk.vm.EVMParameter;
import com.jmtc.tracegoods.domain.AttributeInfo;
import com.jmtc.tracegoods.domain.GoodsInfo;
import com.jmtc.tracegoods.domain.GuestMessageInfo;
import com.jmtc.tracegoods.domain.LogisticInfo;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Chris
 * @date 2021/6/1 21:38
 * @Email:gang.wu@nexgaming.com
 */
public class TrackGoodsChainApi {
    /**
     * contract id
     */
    private static String callContractId = "traceGoods1623134872499";

    private static final String account = "chrisblocktest";
    private static Identity userIdentity;
    private static Keypair userKeypair;

    /**
     * sdk client
     */
    private static MychainClient sdk;

    /**
     * client key password
     */
    private static String keyPassword = "Local#123";
    /**
     * user password
     */
    private static String userPassword = "Local#123";
    /**
     * host ip
     */

    private static String host = "47.103.163.48";

    /**
     * server port
     */
    private static int port = 18130;
    /**
     * trustCa password.
     */
    private static String trustStorePassword = "mychain";
    /**
     * mychain environment
     */
    private static ClientEnv env;
    /**
     * mychain is tee Chain
     */
    private static boolean isTeeChain = false;
    /**
     * tee chain publicKeys
     */
    private static List<byte[]> publicKeys = new ArrayList<byte[]>();
    /**
     * tee chain secretKey
     */
    private static String secretKey = "123456";


    private void initMychainEnv() throws IOException {
        // any user key for sign message
        String userPrivateKeyFile = "user.key";
        userIdentity = Utils.getIdentityByName(account);
        Pkcs8KeyOperator pkcs8KeyOperator = new Pkcs8KeyOperator();
        userKeypair = pkcs8KeyOperator.load(IOUtil.inputStreamToByte(TrackGoodsChainApi.class.getClassLoader().getResourceAsStream(userPrivateKeyFile)), userPassword);

        // use publicKeys by tee
        if (isTeeChain) {
            Keypair keypair = new Pkcs8KeyOperator()
                    .loadPubkey(
                            IOUtil.inputStreamToByte(TrackGoodsChainApi.class.getClassLoader().getResourceAsStream("test_seal_pubkey.pem")));
            byte[] publicKeyDer = keypair.getPubkeyEncoded();
            publicKeys.add(publicKeyDer);
        }

        env = buildMychainEnv();
        ILogger logger = AbstractLoggerFactory.getInstance(TrackGoodsChainApi.class);
        env.setLogger(logger);
    }

    private ClientEnv buildMychainEnv() throws IOException {
        InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved(host, port);
        String keyFilePath = "client.key";
        String certFilePath = "client.crt";
        String trustStoreFilePath = "trustCa";
        // build ssl option
        ISslOption sslOption = new SslBytesOption.Builder()
                .keyBytes(IOUtil.inputStreamToByte(TrackGoodsChainApi.class.getClassLoader().getResourceAsStream(keyFilePath)))
                .certBytes(IOUtil.inputStreamToByte(TrackGoodsChainApi.class.getClassLoader().getResourceAsStream(certFilePath)))
                .keyPassword(keyPassword)
                .trustStorePassword(trustStorePassword)
                .trustStoreBytes(
                        IOUtil.inputStreamToByte(TrackGoodsChainApi.class.getClassLoader().getResourceAsStream(trustStoreFilePath)))
                .build();

        List<InetSocketAddress> socketAddressArrayList = new ArrayList<InetSocketAddress>();
        socketAddressArrayList.add(inetSocketAddress);

        List<SignerBase> signerBaseList = new ArrayList<SignerBase>();
        SignerBase signerBase = MyCrypto.getInstance().createSigner(userKeypair);
        signerBaseList.add(signerBase);
        SignerOption signerOption = new SignerOption();
        signerOption.setSigners(signerBaseList);
        return ClientEnv.build(socketAddressArrayList, sslOption, signerOption);
    }

    private void initSdk() {
        sdk = new MychainClient();
        boolean initResult = sdk.init(env);
        if (!initResult) {
            exit("initSdk", "sdk init failed.");
        }else{
            System.out.println("sdk init success");
        }
    }

    private String getErrorMsg(int errorCode) {
        int minMychainSdkErrorCode = ErrorCode.SDK_INTERNAL_ERROR.getErrorCode();
        if (errorCode < minMychainSdkErrorCode) {
            return ErrorCode.valueOf(errorCode).getErrorDesc();
        } else {
            return ErrorCode.valueOf(errorCode).getErrorDesc();
        }
    }

    private void exit(String tag, String msg) {
        exit(String.format("%s error : %s ", tag, msg));
    }

    private void exit(String msg) {
        System.out.println(msg);
        System.exit(0);
    }

    private void signRequest(AbstractTransactionRequest request) {
        // sign request
        long ts = sdk.getNetwork().getSystemTimestamp();
        request.setTxTimeNonce(ts, BaseFixedSizeUnsignedInteger.Fixed64BitUnsignedInteger
                .valueOf(RandomUtil.randomize(ts + request.getTransaction().hashCode())), true);
        request.complete();
        sdk.getConfidentialService().signRequest(env.getSignerOption().getSigners(), request);
    }


    public GoodsInfo getGoodsInfo(){
        GoodsInfo goodsInfo = null;
        EVMParameter parameters = new EVMParameter("getGoodInfo()");
        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("getGoodsName Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call getGoodsName function", "output failed");
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                goodsInfo = new GoodsInfo();
                goodsInfo.setName(contractReturnValues.getString());
                goodsInfo.setPrice(contractReturnValues.getUint());
                goodsInfo.setWeight(contractReturnValues.getUint());
                System.out.println("call getGoodsName success,name is: " + goodsInfo.getName());
            }
        }
        return goodsInfo ;
    }

    public Boolean putAttribute(String _id,String _name, String _date, String _desc) {
        EVMParameter parameters = new EVMParameter("putAttribute(identity,string,string,string)");
        parameters.addIdentity(Utils.getIdentityByName(_id));
        parameters.addString(_name);
        parameters.addString(_date);
        parameters.addString(_desc);

        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("putAttribute Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
            return false;
        } else {
            System.out.println("call putAttribute success");
            return true;
        }
    }

    public AttributeInfo getAttributeById(Integer aIndex){
        AttributeInfo attributeInfo = null;
        EVMParameter parameters = new EVMParameter("getAttribute(uint256)");
        parameters.addUint(BigInteger.valueOf(aIndex));
        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("getAttribute Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call getAttribute function", "output failed");
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                attributeInfo = new AttributeInfo();
                attributeInfo.setId(contractReturnValues.getIdentity().toString());
                attributeInfo.setName(contractReturnValues.getString());
                attributeInfo.setDate(contractReturnValues.getString());
                attributeInfo.setDesc(contractReturnValues.getString());
                System.out.println("call getAttribute success");
            }
        }

        return attributeInfo ;
    }

    public Boolean putLogistics(String id,String _date, Integer _status, String _message){
        EVMParameter parameters = new EVMParameter("putLogistics(identity,string,uint256,string)");
        parameters.addIdentity(Utils.getIdentityByName(id));
        parameters.addString(_date);
        parameters.addUint(BigInteger.valueOf(_status));
        parameters.addString(_message);

        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("putLogistics Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
            return false;
        } else {
            System.out.println("call putLogistics success");
            return true ;
        }
    }

    public List<LogisticInfo> getLogisticsList(){
        List<LogisticInfo> list = new ArrayList<>();
        long count = getLogisticsCount();
        if(count >0){
            for (int index = 1; index <= count;index ++){
                LogisticInfo info = getLogistics(index);
                if(info!= null){
                    list.add(info);
                }
            }
        }
        return list ;
    }

    private String getStatusDesc(int statusCode){
        String statusDes = "";
        if(statusCode < 0 || statusCode > 5 ){
            statusDes =  "状态异常" ;
            return statusDes;
        }

        if(statusCode == 0){
            statusDes ="Origin";
        }

        if(statusCode == 1){
            statusDes ="屠宰";
        }

        if(statusCode == 2){
            statusDes ="检疫";
        }


        if(statusCode == 3){
            statusDes ="运输";
        }

        if(statusCode == 4){
            statusDes ="签收";
        }

        return  statusDes;
    }

    public LogisticInfo getLogistics(Integer lIndex){
        LogisticInfo logisticInfo = null;
        EVMParameter parameters = new EVMParameter("getLogistics(uint256)");
        parameters.addUint(BigInteger.valueOf(lIndex));
        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("getLogistics Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call getLogistics function", "output failed");
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                String owner = contractReturnValues.getIdentity().toString();
                String date = contractReturnValues.getString();
                String status = contractReturnValues.getUint().toString();
                String message = contractReturnValues.getString();

                logisticInfo = new LogisticInfo();
                logisticInfo.setId(owner);
                logisticInfo.setStatus(Integer.parseInt(status));
                logisticInfo.setDesc(message);
                logisticInfo.setDate(date);
                logisticInfo.setStatusDesc(getStatusDesc(logisticInfo.getStatus()));
                System.out.println(String.format("call getLogistics success,response is %s,%s,%s,%s",owner,date,status,message));
            }
        }

        return logisticInfo ;
    }

    public long getLogisticsCount(){
        long logisticsCount = 0L;
        EVMParameter parameters = new EVMParameter("getLogisticsCount()");
        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("getLogisticsCount Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call getLogisticsCount function", "output failed");
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                logisticsCount = contractReturnValues.getUint().longValue();
                System.out.println("call getLogisticsCount success,count is: " + logisticsCount);
            }
        }
        return logisticsCount ;
    }

    public long getAttributeCount(){
        long attributeCount = 0L;
        EVMParameter parameters = new EVMParameter("getAttributesCount()");
        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("getAttributeCount Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call getAttributeCount function", "output failed");
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                attributeCount = contractReturnValues.getUint().longValue();
                System.out.println("call getAttributeCount success,count is: " + attributeCount);
            }
        }
        return attributeCount ;
    }

    public List<AttributeInfo> getAttributeList(){
        List<AttributeInfo> list = new ArrayList<>();
        long count = getAttributeCount();
        if(count >0){
            for (int index = 1; index <= count;index ++){
                AttributeInfo info = getAttributeById(index);
                if(info!= null){
                    list.add(info);
                }
            }
        }
        return list ;
    }

    public Boolean putGuestMessage(String _name, String _date, String _message) {
        EVMParameter parameters = new EVMParameter("addGuestbook(string,string,string)");
        parameters.addString(_name);
        parameters.addString(_date);
        parameters.addString(_message);

        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("putGuestMessage Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
            return false;
        } else {
            System.out.println("call putGuestMessage success");
            return true;
        }
    }

    public long getMessageCount(){
        long messageCount = 0L;
        EVMParameter parameters = new EVMParameter("getAttributesCount()");
        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("getMessageCount Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call getMessageCount function", "output failed");
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                messageCount = contractReturnValues.getUint().longValue();
                System.out.println("call getMessageCount success,count is: " + messageCount);
            }
        }
        return messageCount ;
    }

    public GuestMessageInfo getMessageById(Integer aIndex){
        GuestMessageInfo messageInfo = null;
        EVMParameter parameters = new EVMParameter("getGuessMessage(uint256)");
        parameters.addUint(BigInteger.valueOf(aIndex));
        CallContractRequest request = new CallContractRequest(userIdentity,
                Utils.getIdentityByName(callContractId), parameters, BigInteger.ZERO, VMTypeEnum.EVM);

        TransactionReceiptResponse callContractResult;
        callContractResult = sdk.getContractService().callContract(request);

        if (!callContractResult.isSuccess() || callContractResult.getTransactionReceipt().getResult() != 0) {
            System.out.println("getMessageById Error :"  + getErrorMsg((int) callContractResult.getTransactionReceipt().getResult()));
        } else {
            byte[] output = callContractResult.getTransactionReceipt().getOutput();
            if (output == null) {
                exit("call getMessageById function", "output failed");
            } else {
                // decode return values
                EVMOutput contractReturnValues = new EVMOutput(ByteUtils.toHexString(output));
                messageInfo = new GuestMessageInfo();
                messageInfo.setName(contractReturnValues.getString());
                messageInfo.setDate(contractReturnValues.getString());
                messageInfo.setMessage(contractReturnValues.getString());
                System.out.println("call getMessageById success");
            }
        }
        return messageInfo ;
    }

    public List<GuestMessageInfo> getMesageList(){
        List<GuestMessageInfo> list = new ArrayList<>();
        long count = getMessageCount();
        if(count >0){
            for (int index = 1; index <= count;index ++){
                GuestMessageInfo info = getMessageById(index);
                if(info!= null){
                    list.add(info);
                }
            }
        }
        return list ;
    }

    public void initAntSDK() throws Exception{
        //step 1:init mychain env.
        initMychainEnv();
        //step 2: init sdk client
        initSdk();
    }

    public void shutDown(){
        sdk.shutDown();
    }
}
