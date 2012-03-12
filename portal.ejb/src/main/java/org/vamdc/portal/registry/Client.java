package org.vamdc.portal.registry;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.vamdc.portal.Settings;
import org.vamdc.registry.client.Registry;
import org.vamdc.registry.client.Registry.Service;
import org.vamdc.registry.client.RegistryCommunicationException;
import org.vamdc.registry.client.RegistryFactory;

public enum Client {
	INSTANCE;
	
	private volatile Registry registry;
	private RegistryThread thread;
	
	Client(){
		registry = new EmptyRegistry();
		Executor exec = Executors.newSingleThreadExecutor();
		thread = new RegistryThread();
		exec.execute(thread);
	}

	public Registry get(){
		return registry;
	}
	
	void set(Registry newInstance){
		this.registry=newInstance;
	}
	
	private class RegistryThread implements Runnable{

		private volatile boolean running=true;
		
		public void run() {
			while(running){
				try {
					reloadRegistry();
					Thread.sleep(Settings.REGISTRY_UPDATE_INTERVAL.getInt());
				} catch (RegistryCommunicationException e) {
					System.err.println("Registry communication problem"+e.getMessage());
					e.printStackTrace();
					try {
						Thread.sleep(Settings.REGISTRY_RETRY_INTERVAL.getInt());
					} catch (InterruptedException e1) {
					}
				} catch (InterruptedException e) {
				}
			}
			System.out.println("registry client thread interrupted");
		}

		public void stop(){
			running=false;
		}
		
		private void reloadRegistry() throws RegistryCommunicationException {
			try{
				Registry newInstance = RegistryFactory.getClient(Settings.REGISTRY_URL.get());
				if (newInstance!=null && newInstance.getIVOAIDs(Service.VAMDC_TAP).size()>0)
					Client.INSTANCE.set(newInstance);
			}catch (Exception e){
				if (!(e instanceof RegistryCommunicationException))
					throw new RegistryCommunicationException(e.getMessage());
				else 
					throw (RegistryCommunicationException)e;
			}
		}
		
	}
	
	void stopUpdates(){
		thread.stop();
	}
	
	
	
	
}
