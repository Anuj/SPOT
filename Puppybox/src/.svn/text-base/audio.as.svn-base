// ActionScript file
	import flash.events.Event;
	import flash.net.URLRequest;
	import flash.media.SoundMixer;
	import mx.collections.ArrayCollection;		
	public var audioQueue:ArrayCollection = new ArrayCollection();

    private function addAudio(m:String):void
    {
    	audioQueue.addItem(m);
    }
    
	private function playIntroAudio():void
	{	   
	   playback();
	}	
	
	private function playback():void
	{		
		SoundMixer.stopAll();
		snd = new Sound();
	   	snd.addEventListener(Event.OPEN, openSoundHandler);	  
		//Alert.show(audioQueue.getItemAt(0).toString());
		var req:URLRequest = new URLRequest(audioQueue.getItemAt(0).toString());
		snd.load(req);
		var channel:SoundChannel = snd.play();
	    channel.addEventListener(Event.SOUND_COMPLETE, introSoundHandler);	
	   	snd.play();	   	
	}
	
	private function introSoundHandler(event:Event):void
	{
		if(audioQueue.length > 1)
		{
			audioQueue.removeItemAt(0);
			playback();
		}
		if(callout!=null)
		{
			hideImage(callout);
			//playVideo();
		}
		if(resImage!=null)
		{
			hideImage(resImage);
		}
	}
	
	
	private function openSoundHandler(event:Event):void
	{
		//determineCharacterMood("talk");
       	//playVideo();
       	callout.source = "./assets/callout.png";
       	showImage(callout);	
	}