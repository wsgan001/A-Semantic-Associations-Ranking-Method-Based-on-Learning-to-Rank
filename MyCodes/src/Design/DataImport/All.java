package Design.DataImport;

import Design.DataImport.Mapping;
import Design.DataImport.Mapping2Types;
import Design.DataImport.Types;
import Design.DataImport.AddIndex;
import Design.DataImport.AddTypes;
import Design.DataImport.TypesVar;
import Design.DataImport.Create;

import java.text.SimpleDateFormat;
import java.util.Date;

import Design.DataImport.AddClassDepCnt;

public class All {

	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("DataImport Begin!" + mat.format(new Date()));
		
		//Initialize
		TypesVar.main(args);
		Create.main(args);
		//AddClass&Topics
		AddClass.main(args);
		AddTopic.main(args);
		//Types
		Types.main(args);
		Mapping2Types.main(args);
		AddTypes.main(args);
		//Mapping
		Mapping.main(args);
		//Index
		AddIndex.main(args);
		//ClassDepCnt
		AddClassDepCnt.main(args);
		
		System.out.println("DataImport End!" + mat.format(new Date()));
	}
}
