package it.polimi.pubsubmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slasoi.common.messaging.MessagingException;
import org.slasoi.common.messaging.Setting;
import org.slasoi.common.messaging.Settings;
import org.slasoi.common.messaging.pubsub.Channel;
import org.slasoi.common.messaging.pubsub.PubSubMessage;
import org.slasoi.common.messaging.pubsub.xmpp.PubSubManager;

public class PubSubProxy {
	
	private Map<String, PubSubManager> managers = new HashMap<String, PubSubManager>();
	
	public void closeAll() {
		managers.clear();
	}
	
	public void publish(ArrayList<String> channels, String output) throws MessagingException {
		
		for (int i = 0; i < channels.size(); i++) {		
			PubSubMessage message = new PubSubMessage(channels.get(i), output);		
			PubSubManager manager;		
			if (managers.containsKey(channels.get(i))) {
				manager = managers.get(channels.get(i));
			}
			else {
				manager = createPSM(channels.get(i));
				managers.put(channels.get(i), manager);
			}
			manager.publish(message);
		}
		
		
		
	}
	
	private PubSubManager createPSM(String channel) {
		// TODO Auto-generated method stub
				
		Settings settings = new Settings();
		settings.setSetting(Setting.xmpp_resource, "ActiveBPEL_Singleton");
		settings.setSetting(Setting.xmpp_username, "");
		settings.setSetting(Setting.xmpp_password, "");
		settings.setSetting(Setting.xmpp_host, "slasoi.dnsalias.net");
		settings.setSetting(Setting.xmpp_service, "slasoi.dnsalias.net");
		settings.setSetting(Setting.pubsub, "xmpp");
		settings.setSetting(Setting.xmpp_pubsubservice, "pubsub.slasoi.dnsalias.net");
		settings.setSetting(Setting.xmpp_port,"5222");
		
		PubSubManager returnPSM = null;
		try {
			 returnPSM= new PubSubManager(settings);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(returnPSM.isChannel(channel)){
			}
			else{
				returnPSM.createChannel(new Channel (channel));
				if(returnPSM.isChannel(channel)){
				}
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnPSM;
	}
	
	

}
