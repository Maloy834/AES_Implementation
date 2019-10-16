package aes;

public class keyExpansion {
	String expandedKey[][]=new String[4][44];
	String temp[][]=new String[4][1];
	SubstitutionBox sbBox= new SubstitutionBox(); 
	public keyExpansion(){
		
	}
	public void startExpansion(String[] key) {
		// TODO Auto-generated constructor stub
		covertKeystringIntoMatrix(key);
		expandkeyfunction();
	}
	private void expandkeyfunction() {
		// TODO Auto-generated method stub
		for(int i=4;i<44;i++)
		{
			temp[0][0]=expandedKey[0][i-1];
			temp[1][0]=expandedKey[1][i-1];
			temp[2][0]=expandedKey[2][i-1];
			temp[3][0]=expandedKey[3][i-1];
			if(i%4==0){
				RotWord();
				SubWord();
				xorWithRcon((i/4)-1);
				//printTemp("After xorWithRcon ");
			}
			if(i%4==4)
			{
				SubWord();
				//printTemp("After subWord "+i+"\n");
			}
			
			xorWithWord(i-4);
			//printTemp("After xorWithW "+i+" \n");
			
			expandedKey[0][i]=temp[0][0];
			expandedKey[1][i]=temp[1][0];
			expandedKey[2][i]=temp[2][0];
			expandedKey[3][i]=temp[3][0];
		}
		
		
	}
	private void xorWithWord(int i) {
		// TODO Auto-generated method stub
			temp[0][0]=xorStringWithString(temp[0][0], expandedKey[0][i]);
			temp[1][0]=xorStringWithString(temp[1][0], expandedKey[1][i]);
			temp[2][0]=xorStringWithString(temp[2][0], expandedKey[2][i]);
			temp[3][0]=xorStringWithString(temp[3][0], expandedKey[3][i]);	
			//printTemp(" "+(i+8)+"---");
		
	}
	private void xorWithRcon(int i) {
		// TODO Auto-generated method stub
		String rCon[][] = new String[][]
				{
					{"01","02","04","08","10","20","40","80","1B","36"},
					{"00","00","00","00","00","00","00","00","00","00"},
					{"00","00","00","00","00","00","00","00","00","00"},
					{"00","00","00","00","00","00","00","00","00","00"},
				};
			
			temp[0][0]=xorStringWithString(temp[0][0], rCon[0][i]);
			temp[1][0]=xorStringWithString(temp[1][0], rCon[1][i]);
			temp[2][0]=xorStringWithString(temp[2][0], rCon[2][i]);
			temp[3][0]=xorStringWithString(temp[3][0], rCon[3][i]);
	}
	public String xorStringWithString(String str, String s)
	{
		int x= Integer.parseInt(str,16)^Integer.parseInt(s,16);
		String rS= Integer.toHexString(x);
		if(rS.length()<2)
			rS="0"+rS;
		//System.out.println("-----"+rS);
		return rS;
		
	}
	private void SubWord() {
		// TODO Auto-generated method stub
		//expandedKey
		//System.out.println("subword");
		//System.out.println(temp[0][0]+" "+temp[1][0]+" "+temp[2][0]+" "+temp[3][0] );
		temp[0][0] =sbBox.getsBox(temp[0][0]);
		temp[1][0] =sbBox.getsBox(temp[1][0]);
		temp[2][0] =sbBox.getsBox(temp[2][0]);
		temp[3][0] =sbBox.getsBox(temp[3][0]);
		
	}
	private void RotWord() {
		// TODO Auto-generated method stub
		String t= temp[3][0];
		temp[3][0]=temp[0][0];
		temp[0][0]=temp[1][0];
		temp[1][0]=temp[2][0];
		temp[2][0]=t;
		
	}
	private void covertKeystringIntoMatrix(String[] key) {
		// TODO Auto-generated method stub
		int pos=0;
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++)
			{
				expandedKey[j][i]=key[pos++];
			}
		}
	}
	void printTemp(String str)
	{
		System.out.println(str+temp[0][0]+" "+temp[1][0]+" "+temp[2][0]+" "+temp[3][0]);
	}
	
}
