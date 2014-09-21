package
{
	import flash.filesystem.File;
	import flash.filesystem.FileMode;
	import flash.filesystem.FileStream;
	
	import mx.collections.ArrayCollection;
	
	public class syllabusReader
	{
		public var syllabusArray:ArrayCollection = new ArrayCollection();	
		public var syllabusItemCounter:int;
		
		public function syllabusReader()
		{
			var file:File = new File("/Users/anuj/Documents/Flex Builder 3/Puppybox/bin-debug/assets/syllabus.xml");			
			//File.documentsDirectory.resolvePath("AIR Test/preferences.xml");
			var fileStream:FileStream = new FileStream();
			fileStream.open(file, FileMode.READ);
			var syllabusXML:XML = XML(fileStream.readUTFBytes(fileStream.bytesAvailable));
			fileStream.close();
			for each (var sItem:XML in syllabusXML.pair)
			{
    			var syllabusitem: syllabusItem = new syllabusItem(sItem);
    			syllabusArray.addItem(syllabusitem);
			}					
			syllabusItemCounter = 0;
		}		
		
		public function getNextSyllabusItem():Object
		{
			var ret:Object = syllabusArray.getItemAt(syllabusItemCounter);
			syllabusItemCounter++;
			return ret;
		}
	}
}