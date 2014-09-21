package
{
	
	import flash.events.*;
	import flash.net.*;
	
	import mx.controls.Alert;
	
	
public class socketConnection extends EventDispatcher
	{
	
	    public static const DATA_CHANGED:String = 'data_changed_event';
		protected var _data:String;
		
		protected var _socketName:String;
		protected var _socketPort:Number;
		public var _socket:Socket;

		
		[Bindable(event=DATA_CHANGED)]
		public function getData():String{
			return _data;	
		}
		
		public function socketConnection(socket:String,port:Number)
		{
			_socketName = socket;
			_socketPort = port;
			_socket = new Socket();
			_socket.addEventListener(Event.CONNECT,onConnect);
			_socket.addEventListener(Event.CLOSE,onDisconnect);
			//_socket.addEventListener(DataEvent.DATA,updateData);
			_socket.addEventListener(IOErrorEvent.IO_ERROR, handleSocketEvent);
			_socket.addEventListener(SecurityErrorEvent.SECURITY_ERROR, handleSocketEvent);
			_socket.addEventListener(ProgressEvent.SOCKET_DATA, socketDataHandler);
			_socket.connect(_socketName,_socketPort);
		}

    protected function socketDataHandler(event:ProgressEvent):void
    {
		var value:String = String(_socket.readUTFBytes(_socket.bytesAvailable));
		_data=value;
		this.dispatchEvent(new Event(DATA_CHANGED));
		//Alert.show(value);
    } 


	protected function onConnect(e:Event):void{
		Alert.show("Connected!");
		
	}
	
	protected function handleSocketEvent(e:Event):void{
		Alert.show("Event:" + e.toString() + "\nTarget:" + e.currentTarget.toString() );			
	}
	
	//this gets triggered when Flash disconnects from Arduino
	protected function onDisconnect(e:Event):void{
		Alert.show("Disconnected!");
	}
	
	/*protected function updateData(de:DataEvent):void{
		_data = de.data;	
		Alert.show(_data);
	}*/
	
	public function sendData(d:String):void{
		_socket.writeUTFBytes(d);
	}
	
	protected function processData(d:String):Object{
			return d;
		}
	}
}