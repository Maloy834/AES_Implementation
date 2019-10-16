package aes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AES {
	String state[][] = new String[4][4];
	keyExpansion kex = new keyExpansion();
	SubstitutionBox sBox = new SubstitutionBox();
	InverseSubstitutionBox iVbox = new InverseSubstitutionBox();
	boolean padding=false;
	public void encryption(String inputfile, String[] key) throws IOException {
		File file = new File(inputfile);
		String line;
		int readBytes;
		byte[] barray = new byte[16];
		file.createNewFile();
		FileInputStream fstream = new FileInputStream(file);
		String outputFileName = inputfile + ".enc";
		PrintWriter writer = null;
		writer = new PrintWriter(outputFileName, "UTF-8");
		while ((readBytes = fstream.read(barray)) != -1) {
			line = new String(barray);
			line = line.substring(0, readBytes);
			int len = line.length();
			if (len < 16) {
				for (int i = 0; i < 16 - len-1; i++) {
					line = line + "0";
				}
				
				String s= Integer.toHexString(16-len);
				padding=true;
				line=line+s;
				//line=line+Integer.toString(t)+Integer.toString(r);
			}
			converInputMatrix(line);
			kex.startExpansion(key);
			// printstate();
			addRoundKey(0);
			// printstate();
			barray = new byte[16];
			for (int i = 1; i <= 10; i++) {
				substituteByte();
				shiftRow();
				if (i != 10) {
					mixColumn();
				}
				addRoundKey(i);
			}
			String ciphertext = columnMatrixToString();
			writer.write(ciphertext);
		}
		writer.close();
		fstream.close();

	}

	private String columnMatrixToString() {
		// TODO Auto-generated method stub
		String str = "";
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				str = str + state[j][i].toUpperCase();
			}
		}
		return str;
	}

	private void mixColumn() {
		// TODO Auto-generated method stub
		int inputmatrix[][] = { { 2, 3, 1, 1 }, { 1, 2, 3, 1 }, { 1, 1, 2, 3 }, { 3, 1, 1, 2 }, };
		for (int i = 0; i < 4; i++) {
			String[] temp = new String[4];
			for (int j = 0; j < 4; j++) {
				// System.out.println(state[0][i]);
				int a = matrixMultipication(state[0][i], inputmatrix[j][0]);
				int b = matrixMultipication(state[1][i], inputmatrix[j][1]);
				// System.out.println("---------------"+Integer.toHexString(b));
				int c = matrixMultipication(state[2][i], inputmatrix[j][2]);
				// System.out.println("---------------"+Integer.toHexString(c));
				int d = matrixMultipication(state[3][i], inputmatrix[j][3]);
				int result = a ^ b ^ c ^ d;
				String s = Integer.toHexString(result);
				if (s.length() == 1)
					s = "0" + s;
				temp[j] = s;
			}
			state[0][i] = temp[0];
			state[1][i] = temp[1];
			state[2][i] = temp[2];
			state[3][i] = temp[3];
		}

	}

	public int matrixMultipication(String s, int b1) {
		int in = Integer.parseInt(s, 16);
		String a = eightBitString(in);
		String b = eightBitString(b1);

		char p = 0;
		byte carry;
		byte IB = 27;

		for (int i = 1; i <= 8; i++) {

			if (Integer.parseInt(a, 2) == 0 || Integer.parseInt(b, 2) == 0)
				break;

			if ((Integer.parseInt(b, 2) & 1) == 1) {
				p ^= Integer.parseInt(a, 2);
			}

			b = eightBitString((Integer.parseInt(b, 2) >>> 1));
			carry = 0;
			if (Integer.parseInt(a, 2) >>> 7 == 1)
				carry = 1;

			a = eightBitString((Integer.parseInt(a, 2) << 1));
			int as = Integer.parseInt(a, 2);
			if (carry == 1) {
				as ^= IB;
				a = eightBitString(as);
			}

		}
		return p;

	}

	String eightBitString(int n) {
		String a = Integer.toBinaryString(n);
		int l = a.length();
		if (l < 8) {
			for (int i = 0; i < 8 - l; i++)
				a = "0" + a;
		} else if (l > 8) {
			a = a.substring(l - 8);
		}
		return a;
	}

	private void shiftRow() {
		// TODO Auto-generated method stub
		String temp = state[1][0];
		state[1][0] = state[1][1];
		state[1][1] = state[1][2];
		state[1][2] = state[1][3];
		state[1][3] = temp;

		temp = state[2][0];
		state[2][0] = state[2][2];
		state[2][2] = temp;
		temp = state[2][1];
		state[2][1] = state[2][3];
		state[2][3] = temp;

		temp = state[3][3];
		state[3][3] = state[3][2];
		state[3][2] = state[3][1];
		state[3][1] = state[3][0];
		state[3][0] = temp;
	}

	private void substituteByte() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				state[i][j] = sBox.getsBox(state[i][j]);
			}
		}
	}

	private void addRoundKey(int round) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				state[i][j] = kex.xorStringWithString(state[i][j], kex.expandedKey[i][j + (round * 4)]);
			}
		}

	}

	private void converInputMatrix(String line) {
		// TODO Auto-generated method stub
		int pos = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				char ch = line.charAt(pos++);
				int aschiValue = (int) ch;
				state[j][i] = Integer.toHexString(aschiValue);

			}
		}
	}

	void printstate() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(state[i][j].toUpperCase());
			}
			System.out.println();
		}
	}

	public void decryption(String decryptFile, String[] key) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println("--------------" + " decryption:");
		File file = new File(decryptFile);
		state = new String[4][4];
		String line;
		int readBytes;
		byte[] barray = new byte[32];
		file.createNewFile();
		FileInputStream fstream = new FileInputStream(file);
		String outputFileName = decryptFile + ".dec";
		int t= (int) file.length();
		//System.out.println("output file size :"+t);
		PrintWriter writer = null;
		writer = new PrintWriter(outputFileName, "UTF-8");
		int bytesum =0;
		
		while ((readBytes = fstream.read(barray)) != -1) {
			bytesum+=readBytes;
			line = new String(barray);
			line = line.substring(0, readBytes);
			int len = line.length();
			if (len < 32) {
				for (int i = 0; i < 32 - len; i++) {
					line = line + "0";
				}
			}
			convertDecryptInputMatrix(line);
			for (int i = 10; i >= 1; i--) {
				addRoundKey(i);
				if (i != 10) {
					inverseMixColumn();

				}

				inverseShiftRow();
				inverseSubByte();
			}
			addRoundKey(0);
			String hexText = columnMatrixToString();
			String plainText;
			if(padding && bytesum==t){
				plainText=convertHexToAscii(hexText,true);
			}
			else{
				plainText=convertHexToAscii(hexText,false);
			}
			//System.out.println("The Plain Text is : " + plainText);
			writer.write(plainText);
			// printstate();
		}
		
		writer.close();
		fstream.close();
	}

	private String convertHexToAscii(String hexText, boolean check) {
		// TODO Auto-generated method stub
		StringBuilder output= new StringBuilder("");
		int decimal= hexText.length();
		if(check){
			String str=hexText.substring(hexText.length()-2,hexText.length());
			char n=(char) Integer.parseInt(str.toUpperCase(),16);
			str=Character.toString(n);
			int temp= Integer.parseInt(str,16)*2;
			decimal= decimal-temp;
			//System.out.println(decimal);
		}
		 for (int i = 0; i <decimal; i += 2) {
		        String str = hexText.substring(i, i + 2);
		        output.append((char) Integer.parseInt(str, 16));
		    }
		return output.toString();
	}

	private void convertDecryptInputMatrix(String line) {
		// TODO Auto-generated method stub
		int k = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				
				state[j][i] = line.charAt(k++) + "" + line.charAt(k++);
			}
		}
	}

	private void inverseSubByte() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				state[i][j] = iVbox.getInverseSBox(state[i][j]);
			}
		}
	}

	private void inverseShiftRow() {
		// TODO Auto-generated method stub
		String temp = state[1][3];
		state[1][3] = state[1][2];
		state[1][2] = state[1][1];
		state[1][1] = state[1][0];
		state[1][0] = temp;

		temp = state[2][3];
		state[2][3] = state[2][1];
		state[2][1] = temp;
		temp = state[2][2];
		state[2][2] = state[2][0];
		state[2][0] = temp;

		temp = state[3][0];
		state[3][0] = state[3][1];
		state[3][1] = state[3][2];
		state[3][2] = state[3][3];
		state[3][3] = temp;
	}

	private void inverseMixColumn() {
		// TODO Auto-generated method stub
		int inversematrix[][] = new int[][] { { 14, 11, 13, 9 }, { 9, 14, 11, 13 }, { 13, 9, 14, 11 },
				{ 11, 13, 9, 14 }, };

		for (int i = 0; i < 4; i++) {
			String temp[] = new String[4];
			for (int j = 0; j < 4; j++) {
				int a = matrixMultipication(state[0][i], inversematrix[j][0]);
				// System.out.println("---------------"+Integer.toHexString(a));
				int b = matrixMultipication(state[1][i], inversematrix[j][1]);
				// System.out.println("---------------"+Integer.toHexString(b));
				int c = matrixMultipication(state[2][i], inversematrix[j][2]);
				// System.out.println("---------------"+Integer.toHexString(c));
				int d = matrixMultipication(state[3][i], inversematrix[j][3]);
				// System.out.println("---------------"+Integer.toHexString(d));
				int result = a ^ b ^ c ^ d;
				String s = Integer.toHexString(result);
				if (s.length() == 1)
					s = "0" + s;
				temp[j] = s;
			}
			state[0][i] = temp[0];
			state[1][i] = temp[1];
			state[2][i] = temp[2];
			state[3][i] = temp[3];
		}

	}
}
