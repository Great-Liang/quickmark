package com.cwp.cmoneycharge;

/**
 *  @author loiy
 *  www.agrilink.cn
 *  2012.11.30   
 */
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.cwp.chart.ConvertNum;

public class DigitUtil {

	/**
	 * �����е������ַ�
	 */
	private static final Character[] SCDigits = { '��', 'һ', '��', '��', '��', '��',
			'��', '��', '��', '��', '��'
	// , '��', 'Ҽ', '��', '��', '��', '��', '½', '��',
	// '��', '��'
	};

	/**
	 * ����������
	 */
	private static final Character[] araDigits = { '0', '1', '2', '2', '3',
			'4', '5', '6', '7', '8', '9'
	// , '0', '1', '2', '3', '4', '5', '6',
	// '7', '8', '9'
	};

	/**
	 * ����������
	 */
	private static Hashtable<Character, Character> araHash = new Hashtable<Character, Character>();

	/**
	 * ����������
	 */
	private static Hashtable<Character, Integer> YWQBSHash = new Hashtable<Character, Integer>();

	static {
		for (int i = 0; i < SCDigits.length; i++) {
			araHash.put(SCDigits[i], araDigits[i]);
		}
		YWQBSHash.put('��', 100000000);
		YWQBSHash.put('��', 10000);
		// YWQBSHash.put('Ǫ', 1000);
		// YWQBSHash.put('��', 100);
		// YWQBSHash.put('ʰ', 10);
		YWQBSHash.put('ǧ', 1000);
		YWQBSHash.put('��', 100);
		YWQBSHash.put('ʮ', 10);
	}

	/**
	 * @param remain
	 *            ʣ�¶��ٸ��ַ�
	 * @param curchar
	 *            ��ǰ�������ַ�
	 */
	private static int ywqbs(int num, Boolean bool, int no, int remain,
			char curchar, char lastshow) {
		if (num == 0) {
			num += no;
			return num;
		} else {
			String numString = String.valueOf(num); // ��numת����String
			String last = numString.substring(numString.length() - 1,
					numString.length()); // ��ȡ���һ���ַ�
			String exceptlast = numString.substring(0, numString.length() - 1); // ��ȡ�������һ���ַ������ַ���
			if (exceptlast.length() == 0) { // ˵��num�Ǹ�λֵ
				num = Integer.parseInt(last) * no;
				return num;
			} else {
				/***** �������������Щ�ַ�[ʮ��ǧ����] start *****/
				if (bool && YWQBSHash.get(curchar) > YWQBSHash.get(lastshow)) { // ˵���ϸ��ַ�����[ʮ��ǧ����]
					num *= no;
					return num;
				}
				/***** �������������Щ�ַ�[ʮ��ǧ����] end *****/
				/***** ���������һ���ַ� start *****/
				/**** ����������(һǧ���ٶ�ʮһ��) ****/
				if (remain == 1
						&& YWQBSHash.get(curchar) > YWQBSHash.get(lastshow)) {
					num *= YWQBSHash.get(curchar);
					return num;
				}
				/**** ����������(һǧ���ٶ�ʮһ��) ****/
				/***** ���������һ���ַ� end *****/
				/***** last���Ϊ0 start *****/
				if (last.equals("0")) { // ѭ�������һλ�ַ�,last����0
					last = "1"; //
				}
				/***** last���Ϊ0 end *****/
				exceptlast = exceptlast + "0"; // ȱ�����һλ,��Ҫ����
				num = Integer.parseInt(exceptlast) + Integer.parseInt(last)
						* no;
				return num;
			}
		}
	}

	public static int parse(String word) {
		int num = 0;
		char lastchar = 'һ'; // �ϴ��ַ�,�ַ���ָ[ʮ��ǧ����],Ĭ�������дһ��'һ'
		char lastshow = 'һ'; // �ϴγ��ֵ��ַ�,�ַ���ָ[ʮ��ǧ����],Ĭ�������дһ��'һ'
		char[] ch = word.toCharArray();
		Boolean bool = false; // �Ƿ���������[ʮ��ǧ����]
		for (int i = 0; i < ch.length; i++) {
			Character find = araHash.get(ch[i]);// ��ȡ��������������[1,2,3...]
			if (find != null) {
				num += Integer.parseInt(String.valueOf(find.charValue()));
				bool = false;
				lastchar = 'һ'; // �ָ���Ĭ��ֵ
				continue;
			} else if (ch[i] == 'ʮ' || ch[i] == 'ʰ') {
				num = ywqbs(num, bool, 10, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = 'ʮ';
				lastshow = 'ʮ';
			} else if (ch[i] == 'ǧ' || ch[i] == 'Ǫ') {
				num = ywqbs(num, bool, 1000, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = 'ǧ';
				lastshow = 'ǧ';
			} else if (ch[i] == '��' || ch[i] == '��') {
				num = ywqbs(num, bool, 100, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '��';
				lastshow = '��';
			} else if (ch[i] == '��') {
				num = ywqbs(num, bool, 10000, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '��';
				lastshow = '��';
			} else if (ch[i] == '��') {
				num = ywqbs(num, bool, 100000000, ch.length - i, ch[i],
						lastshow);
				bool = true;
				lastchar = '��';
				lastshow = '��';
			}
		}
		ch = null;
		return num;
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	// û�п����׼�����λ�����
	public static long chn2digit(String chnStr) {
		// init map
		java.util.Map<String, Integer> unitMap = new java.util.HashMap<String, Integer>();
		unitMap.put("ʮ", 10);
		unitMap.put("��", 100);
		unitMap.put("ǧ", 1000);
		unitMap.put("��", 10000);
		unitMap.put("��", 100000000);

		java.util.Map<String, Integer> numMap = new java.util.HashMap<String, Integer>();
		numMap.put("��", 0);
		numMap.put("һ", 1);
		numMap.put("��", 2);
		numMap.put("��", 3);
		numMap.put("��", 4);
		numMap.put("��", 5);
		numMap.put("��", 6);
		numMap.put("��", 7);
		numMap.put("��", 8);
		numMap.put("��", 9);

		// ����
		List<Long> queue = new ArrayList<Long>();
		long tempNum = 0;
		for (int i = 0; i < chnStr.length(); i++) {
			char bit = chnStr.charAt(i);
			// ����
			if (numMap.containsKey(bit + "")) {

				tempNum = tempNum + numMap.get(bit + "");

				// һλ����ĩλ�����ڻ����ǰһλ������
				if (chnStr.length() == 1
						| i == chnStr.length() - 1
						| (i + 1 < chnStr.length() && (chnStr.charAt(i + 1) == '��' | chnStr
								.charAt(i + 1) == '��'))) {
					queue.add(tempNum);
				}
			}
			// ��λ
			else if (unitMap.containsKey(bit + "")) {

				// ����ʮ ת��Ϊһʮ����ʱ����������
				if (bit == 'ʮ') {
					if (tempNum != 0) {
						tempNum = tempNum * unitMap.get(bit + "");
					} else {
						tempNum = 1 * unitMap.get(bit + "");
					}
					queue.add(tempNum);
					tempNum = 0;
				}

				// ����ǧ���� ��ʱ����������
				if (bit == 'ǧ' | bit == '��') {
					if (tempNum != 0) {
						tempNum = tempNum * unitMap.get(bit + "");
					}
					queue.add(tempNum);
					tempNum = 0;
				}

				// �����ڡ��� �����и�Ԫ�������ۼ�*��λֵ����ն��С��½��ֵ������
				if (bit == '��' | bit == '��') {
					long tempSum = 0;
					if (queue.size() != 0) {
						for (int j = 0; j < queue.size(); j++) {
							tempSum += queue.get(j);
						}
					} else {
						tempSum = 1;
					}
					tempNum = tempSum * unitMap.get(bit + "");
					queue.clear();// ��ն���
					queue.add(tempNum);// �½��ֵ������
					tempNum = 0;
				}
			}
		}

		// output
		long sum = 0;
		for (Long i : queue) {
			sum += i;
		}
		return sum;
	}

	public String mixnumtostring(String str) {
		str = str.replaceAll("إ", "��ʮ");

		str = str.replaceAll("��", "��");
		str = str.replaceAll("��", "��");
		str = str.replaceAll("��", "��");
		str = str.replaceAll("��", "��");
		str = str.replaceAll("½", "��");
		str = str.replaceAll("��", "��");
		str = str.replaceAll("��", "��");
		str = str.replaceAll("��", "��");
		str = str.replaceAll("��", "��");
		str = str.replaceAll("Ҽ", "һ");

		str = str.replaceAll("Ǫ", "ǧ");
		str = str.replaceAll("��", "��");
		str = str.replaceAll("ʰ", "ʮ");
		str = str.replaceAll("��", "|��|");
		str = str.replaceAll("��", "|��|");
		str = str.replaceAll("ǧ", "|ǧ|");
		str = str.replaceAll("��", "|��|");
		str = str.replaceAll("ʮ", "|ʮ|");
		String regex = "\\|";
		String[] strs = str.split(regex);
		ConvertNum cn = new ConvertNum();
		for (int i = 0; i < strs.length; i++) {
			if (isNumeric(strs[i])) {
				if (!strs[i].equals("")) {
					// strs[i] = d.test(Integer.parseInt(strs[i]));
					strs[i] = cn.NumToChinese(Double.parseDouble(strs[i]));
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]);
		}
		long num = chn2digit(sb.toString());
		return Long.toString(num);
	}

	static void sort(int arry[]) {
		// int arry[] = { 1, 9, 3, 4, 5, 7, 2, 0, 6, 8 };
		int temp = 0;
		int i, j;
		for (i = 1; i <= 10; i++)
			for (j = 1; j < 11 - i; j++)
				if (arry[j] < arry[j - 1]) {
					temp = arry[j];
					arry[j] = arry[j - 1];
					arry[j - 1] = temp;
				}

		// for (i = 0; i < 10; i++)
		// System.out.println(arry[i]);

	}

	// public static void main(String args[]) {
	// Test d = new Test();
	// String str = "30200��";
	// str = str.replaceAll("��", "��");
	//
	// str = str.replaceAll("��", "��");
	// str = str.replaceAll("��", "��");
	// str = str.replaceAll("��", "��");
	// str = str.replaceAll("½", "��");
	// str = str.replaceAll("��", "��");
	// str = str.replaceAll("��", "��");
	// str = str.replaceAll("��", "��");
	// str = str.replaceAll("��", "��");
	// str = str.replaceAll("Ҽ", "һ");
	//
	// str = str.replaceAll("Ǫ", "ǧ");
	// str = str.replaceAll("��", "��");
	// str = str.replaceAll("ʰ", "ʮ");
	// str = str.replaceAll("��", "|��|");
	// str = str.replaceAll("��", "|��|");
	// str = str.replaceAll("ǧ", "|ǧ|");
	// str = str.replaceAll("��", "|��|");
	// str = str.replaceAll("ʮ", "|ʮ|");
	// System.out.println(str);
	// String regex = "\\|";
	// String[] strs = str.split(regex);
	// ConvertNum cn = new ConvertNum();
	// // System.out.println("asd " + cn.NumToChinese(300));
	// for (int i = 0; i < strs.length; i++) {
	// System.out.println(i + " " + strs[i]);
	// if (isNumeric(strs[i])) {
	// if (!strs[i].equals("")) {
	// strs[i] = cn.NumToChinese(Double.parseDouble(strs[i]));
	// }
	// }
	// }
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < strs.length; i++) {
	// sb.append(strs[i]);
	// }
	// System.out.printf(sb.toString());
	// long num = chn2digit(sb.toString());
	// System.out.printf("" + num);
	// }
}