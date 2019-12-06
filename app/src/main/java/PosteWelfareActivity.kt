import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.conio.postequorum.implementation.Configuration
import com.conio.postequorum.implementation.SDKFactory
import org.json.JSONObject
import org.web3j.crypto.CipherException
import org.web3j.crypto.Keys
import org.web3j.crypto.Wallet
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException


@SuppressLint("Registered")
class PosteWelfareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(com.ey.pwbc.R.layout.activity_main)
        val conf = Configuration.ConfigurationBuilder(
            "0x7eCb3410eB7644076b2992E9DB4A9F12a4fed12C",
            "https://a0833b7c.ngrok.io"
        ).build()
        //Create SDK instance
        val sdk = SDKFactory.getInstance().createSDK(conf)

        /*val seed = randomUUID().toString()
        val result = process(seed) // get a json containing private key and address


        Log.d("sos", "seed: " + seed + "result-----" + result)
*/

        //Save keys
        val keysToBeSavedInSecureStorage = sdk.keyPair.serializePrivateKey()

        //Sdk finds all common methods
        sdk.myTokenBalance();
        sdk.myVouchersBalance();
        sdk.myVouchersList();

        //To call Post Office methods, you must call the post() method on sdk and then invoke the Smart Contract method
        //It will be a mistake handled by the EVM and not by the JVM

        sdk.poste().startContract();
        sdk.poste().settlementList;

        //To call Merchant methods:
        sdk.merchant().myEmittedVouchersBalance();
        sdk.merchant().myEmittedVouchersList();
        //To call Employee methods
        sdk.dipendente().recoveryAmount();

        //In the event of an app outage, to retrieve the sdk while keeping the private and public key unchanged
        //the application will need to create an SDK instance by passing in the appropriately saved private key earlier


    }

    private fun process(seed: String): JSONObject {

        val processJson = JSONObject()

        try {
            val ecKeyPair = Keys.createEcKeyPair()
            val privateKeyInDec = ecKeyPair.privateKey

            val sPrivatekeyInHex = privateKeyInDec.toString(16)

            val aWallet = Wallet.createLight(seed, ecKeyPair)
            val sAddress = aWallet.address


            processJson.put("address", "0x$sAddress")
            processJson.put("privatekey", sPrivatekeyInHex)


        } catch (e: CipherException) {
            //
        } catch (e: InvalidAlgorithmParameterException) {
            //
        } catch (e: NoSuchAlgorithmException) {
            //
        } catch (e: NoSuchProviderException) {
            //
        }

        return processJson
    }


}
