package com.cwp.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cwp.cmoneycharge.AddPay;
import com.cwp.cmoneycharge.DigitUtil;
import com.cwp.cmoneycharge.MainActivity;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.Tb_income;
import cwp.moneycharge.model.Tb_pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

public class Util {
	static String[][] mztype = { { "���", "���ϳ�", "����ʳ", "��ʳ" },
			{ "���", "���ϳ�", "����ʳ" }, { "���", "�����" }, { "ҹ��", "��ҹ" },
			{ "������Ʒ", "��ԡ¶", "������", "ϴͷˮ" },
			{ "���", "���⳵", "�Ƴ̳�", "���ִ�", "�Ų�" }, { "���Ӳ�Ʒ", "IPHONE", "����" },
			{ "�����", "��", "��" }, { "���", "�ⷿ" } };
	static String[] number = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"0", "һ", "��", "��", "��", "��", "��", "��", "��", "��", "��", "ʮ", "��",
			"ǧ", "��", "��", "ʰ", "��", "Ǫ", "Ҽ", "��", "��", "��", "��", "½", "��",
			"��", "��" };
	static char[] number2 = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
			'һ', '��', '��', '��', '��', '��', '��', '��', '��', '��', 'ʮ', '��', 'ǧ',
			'��', '��', 'ʰ', '��', 'Ǫ', 'Ҽ', '��', '��', '��', '��', '½', '��', '��',
			'��' };
	static String[] money = { "Ԫ", "��", "Ǯ", "�����", "rmb", "RMB" };
	static String[] voice_pay = { "��", "��", "��", "����", "ʳ" };
	static String[] voice_income = { "��", "��" };
	public static String[] VoiceSave;
	public static String type;
	private static Intent intent;
	private static int userid = 100000001;
	private static List<Tb_income> list_income;
	private static List<Tb_pay> list_pay;
	private static int ip_sum;
	private static int circle1;
	private static int circle2;
	private static int circle3;

	static final boolean IS_JBMR2 = Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2;
	static final boolean IS_ISC = Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	static final boolean IS_GINGERBREAD_MR1 = Build.VERSION.SDK_INT == Build.VERSION_CODES.GINGERBREAD_MR1;

	/*
	 * ʶ����������
	 * 
	 * @param VoiceSave[0] ��������ֵ
	 * 
	 * @param VoiceSave[1] ����ֵ
	 * 
	 * @param VoiceSave[3] �ظ�����ֵ����������ʾ����
	 * 
	 * @param VoiceSave[4] ֧������ֵ
	 * 
	 * @param VoiceSave[5] "����ʶ��"����ֵ
	 */
	public static String Recognition(String t, Context context, int userid) {
		VoiceSave = null;
		VoiceSave = new String[6];
		PtypeDAO ptypeDAO = new PtypeDAO(context);
		ItypeDAO itypeDAO = new ItypeDAO(context);

		int mfirst = t.length() + 1, mend = t.length() + 1, temp = 0;
		Boolean ismoney = false, intype = false, outtype = false;
		Boolean voice_ptype = false, voice_intype = false, isrepet = false, isunit = false;
		String w = "", strmoney = "", inname = "1", outname = "2";
		List<String> spdatalist = ptypeDAO.getPtypeName(userid);
		List<String> spdatalist2 = itypeDAO.getItypeName(userid);
		VoiceSave[2] = t;

		for (int i = 0; i < spdatalist.size(); i++) { // �ж��Ƿ����֧��
			if (t.indexOf(spdatalist.get(i).toString()) > -1) {
				type = "pay";
				intype = true;
				inname = spdatalist.get(i).toString();
				VoiceSave[0] = Integer.toString(i); // VoiceSave[0]Ϊ��������ֵ
			}
		}
		for (int i = 0; i < spdatalist2.size(); i++) { // �ж��Ƿ��������
			if (t.indexOf(spdatalist2.get(i).toString()) > -1) {
				type = "income";
				outtype = true;
				outname = spdatalist2.get(i).toString();
				VoiceSave[4] = Integer.toString(i); // VoiceSave[4]Ϊ֧������ֵ
			}
		}
		if (!(intype || outtype)) {
			for (int i = 0; i < mztype.length; i++) {
				for (int j = 0; j < mztype[i].length; j++) {
					if (t.indexOf(mztype[i][j].toString()) > -1) {
						// System.out.println("���� " + mztype[i][j]);
						for (int k = 0; k < spdatalist.size(); k++) { // �ж��Ƿ����֧��
							if (mztype[i][0].indexOf(spdatalist.get(k)
									.toString()) > -1) {
								type = "pay";
								intype = true;
								inname = spdatalist.get(k).toString();
								VoiceSave[0] = Integer.toString(k); // VoiceSave[0]Ϊ��������ֵ
							}
						}
						for (int l = 0; l < spdatalist2.size(); l++) { // �ж��Ƿ��������
							if (mztype[i][0].indexOf(spdatalist2.get(l)
									.toString()) > -1) {
								type = "income";
								outtype = true;
								outname = spdatalist2.get(l).toString();
								VoiceSave[4] = Integer.toString(l); // VoiceSave[4]Ϊ֧������ֵ
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < voice_pay.length; i++) { // �ж��Ƿ����֧���Ķ���
			if (t.indexOf(voice_pay[i]) > -1) {
				voice_ptype = true;
				type = "pay";
			}
		}
		for (int i = 0; i < voice_income.length; i++) { // �ж��Ƿ����֧���Ķ���
			if (t.indexOf(voice_income[i]) > -1) {
				voice_intype = true;
				type = "income";
			}
		}
		for (int i = 0; i < money.length; i++) { // �ж��Ƿ������λ����ý�β
			if (t.indexOf(money[i]) > -1) {
				temp = t.indexOf(money[i]);
				isunit = true;
				if (temp < mend) {
					mend = temp;
				}
			}
		}
		if (!isunit) { // �޽�β
			int tmend = -2;
			for (int i = 0; i < number.length; i++) { // �ж��Ƿ�������������ֿ�ͷ����ý�β
				if (t.lastIndexOf(number[i]) > -1) {
					temp = t.lastIndexOf(number[i]);
					if (temp > tmend) {
						tmend = temp;
					}
				}
			}
			mend = tmend + 1;
			for (int i = 0; i < number.length; i++) { // �ж��Ƿ�������������ֽ���ÿ�ͷ
				if (t.indexOf(number[i]) > -1) {
					temp = t.indexOf(number[i]);
					if ((temp < mfirst) && (temp <= mend)) {
						mfirst = temp;
					}
				}
			}
		} else {// �н�β
			char[] c = t.toCharArray();
			int flag = t.length() + 1;
			boolean b = true;
			for (int i = mend; i > 0; i--) { // �ж��Ƿ�������������ֽ���ÿ�ͷ
				if (b) {
					for (int j = 0; j < number.length; j++) {
						if (c[i] == number2[j]) {
							flag = i;
							boolean a = true;
							for (int k = 0; k < number.length; k++) {
								if (c[i - 1] == number2[k]) {
									flag = i - 1;
									a = false;
								}
							}
							if (a) {
								b = false;
							}
						}
					}
				} else {
					break;
				}
			}
			mfirst = flag;
		}
		if (!(mfirst == (t.length() + 1) || mend == (t.length() + 1) || mend == -1)) { // ת��Ϊ����������
			ismoney = true;
			strmoney = t.substring(mfirst, mend);
//			System.out.println("strmoney " + strmoney + " mfirst " + mfirst
//					+ " mend " + mend);
			// �ж�����Ƿ����������
			char[] chs = strmoney.toCharArray();
			List<String> num = Arrays.asList(number);
			List<String> mon = Arrays.asList(money);
			for (int l = 0; l < chs.length; l++)
				if (!num.contains(String.valueOf(chs[l])))
					if (!mon.contains(String.valueOf(chs[l])))
						ismoney = false;
			if (ismoney) {
				DigitUtil Util = new DigitUtil();
				VoiceSave[1] = Util.mixnumtostring(strmoney); // ���ù����ദ���ֵĽ��
			}
		}
		if (intype && outtype) { // ���ͬʱ��������/֧�������
			if (outname.equals(inname)) {
				if (ismoney) {
					if (voice_intype || voice_ptype) {
						// dialogShowUtil.dialogShow("rotatebottom", "OK", t,
						// w);
						return "OK ";
					} else {
						VoiceSave[3] = outname; // VoiceSave[3]Ϊ�ظ�����ֵ����������ʾ����
						w = "��ʾ����ѡ��<" + outname + ">Ϊ���뻹��֧��";
						// dialogShowUtil.dialogShow("shake", "judge", t, w); //
						// ������н��
						return "judge " + w;
					}
				} else {
					w = "��ʾ����Ļ���û�а������ѻ�֧��<���>";
					// dialogShowUtil.dialogShow("shake", "wrong", t, w);
					return "wrong " + w;
				}
			} else {
				w = "**��ʾ��һ��ֻ�ܼ�¼һ����¼Ŷ"; // ����������벢��֧�������
				// dialogShowUtil.dialogShow("shake", "wrong", t, w);
				return "wrong " + w;
			}
		} else {
			if (!((intype || outtype) || ismoney)) { // ����������
				w = "**��ʾ��û�а�����<���>\n**��ʾ��\nû�а���<���>\n������У�"
						+ listToString(spdatalist, '��') + "��"
						+ listToString(spdatalist2, '��') + "��";
				// dialogShowUtil.dialogShow("shake", "wrong", t, w);
				return "wrong " + w;
			} else if ((intype || outtype) && (!ismoney)) {
				w = "��ʾ��\nû�а������ѻ�֧��<���>\n���߳��ֶ��<���>";
				// dialogShowUtil.dialogShow("shake", "wrong", t, w);
				return "wrong " + w;
			} else if ((!(intype || outtype)) && ismoney) {
				for (int i = 0; i < spdatalist.size(); i++) { // �ж��Ƿ����֧��
					if ("����ʶ��".indexOf(spdatalist.get(i).toString()) > -1) {
						VoiceSave[5] = Integer.toString(i);
						VoiceSave[3] = "����ʶ��";
					}
				}
				if (voice_intype || voice_ptype) {
					// dialogShowUtil.dialogShow("rotatebottom", "OK", t,
					// w);
					return "notype ";
				} else {
					w = "�����¼Ϊ<����ʶ��>���\n**��ʾ��û�а���<��Ĭ�ϣ����>��"
							+ listToString(spdatalist, '��') + "��\n\n\n";
					// dialogShowUtil.dialogShow("shake", "notype", t, w);
					return "judge " + w;
				}
			} else {
				// dialogShowUtil.dialogShow("rotatebottom", "OK", t, w);
				return "OK " + w;
			}
		}
	}

	private static String setTimeFormat(String newtxtTime) { // �������ڸ�ʽ
		final Calendar c = Calendar.getInstance();// ��ȡ��ǰϵͳ����
		int mYear = c.get(Calendar.YEAR);// ��ȡ���
		int mMonth = c.get(Calendar.MONTH);// ��ȡ�·�
		int mDay = c.get(Calendar.DAY_OF_MONTH);// ��ȡ����
		String date = new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).toString();
		int y, m, d;
		String sm, sd;
		int i = 0, j = 0, k = 0;
		for (i = 0; i < date.length(); i++) {
			if (date.substring(i, i + 1).equals("-") && j == 0)
				j = i;
			else if (date.substring(i, i + 1).equals("-"))
				k = i;
		}
		y = Integer.valueOf(date.substring(0, j));
		m = Integer.valueOf(date.substring(j + 1, k));
		d = Integer.valueOf(date.substring(k + 1));
		if (m < 10) {
			sm = "0" + String.valueOf(m);
		} else
			sm = String.valueOf(m);
		if (d < 10) {
			sd = "0" + String.valueOf(d);
		} else
			sd = String.valueOf(d);
		return String.valueOf(y) + "-" + sm + "-" + sd;
	}

	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
			if (i < list.size() - 1) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	public static int save(final Context context, String type, int userid,
			String strMoney, int Position, String textreAddres,
			String textreMark, String textphoto) {
		// System.out.println("type " + type + " userid " + userid +
		// " strMoney "
		// + strMoney + " Position " + Position + " textreAddres "
		// + textreAddres + " textreMark " + textreMark + " textphoto "
		// + textphoto + " setTimeFormat " + setTimeFormat(null));
		PayDAO payDAO = new PayDAO(context);
		IncomeDAO incomeDAO = new IncomeDAO(context);
		String typemode = "add";
		// Ϊ���水ť���ü����¼�
		if (typemode.equals("add")) { // ���ģʽ
			if (type.equals("pay")) { // ֧��
				// ����InaccountDAO����
				// PayDAO payDAO = new PayDAO(context);
				// ����Tb_inaccount����
				Tb_pay tb_pay = new Tb_pay(userid, payDAO.getMaxNo(userid) + 1,
						AddPay.get2Double(strMoney), setTimeFormat(null),
						Position, textreAddres, textreMark, textphoto);
				payDAO.add(tb_pay);// ���������Ϣ
				new Thread() {
					public void run() {
						Looper.prepare();
						Toast.makeText(context, "������֧����������ӳɹ���",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					};
				}.start();
				intent = new Intent(context, MainActivity.class);
				intent.putExtra("cwp.Fragment", "1");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				return 1;
			} else { // ����
				Tb_income tb_income = new Tb_income(userid,
						incomeDAO.getMaxNo(userid) + 1,
						AddPay.get2Double(strMoney), setTimeFormat(null),
						Position,
						// txtInhandler.getText().toString(),
						textreAddres, textreMark, textphoto, "֧��");
				incomeDAO.add(tb_income);// ���������Ϣ
				// ������Ϣ��ʾ
				new Thread() {
					public void run() {
						Looper.prepare();
						Toast.makeText(context, "���������롽������ӳɹ���",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					};
				}.start();
				intent = new Intent(context, MainActivity.class);
				intent.putExtra("cwp.Fragment", "1");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				return 1;
			}
		}
		return 0;
	}

	public static String gettotal(final Context context, float budget) {
		IncomeDAO incomeDAO = new IncomeDAO(context);
		PayDAO payDAO = new PayDAO(context);

		final Calendar c = Calendar.getInstance();// ��ȡ��ǰϵͳ����
		int defaultMonth = c.get(Calendar.MONTH) + 1;// ��ȡ�·�
		int defaultYear = c.get(Calendar.YEAR);// ��ȡ���
		String dmonth;
		int pay_sum = 0;
		int income_sum = 0;

		if (defaultMonth < 10) {
			dmonth = "0" + Integer.toString(defaultMonth);
		} else {
			dmonth = Integer.toString(defaultMonth);
		}
		String date1 = Integer.toString(defaultYear) + "-" + dmonth + "-01";
		String date2 = Integer.toString(defaultYear) + "-" + dmonth + "-31";
		list_income = incomeDAO.getScrollData(userid, 0, // ȡÿ�����������
				(int) incomeDAO.getCount(userid), date1, date2);
		list_pay = payDAO.getScrollData(userid, 0, // ȡÿ���֧������
				(int) payDAO.getCount(userid), date1, date2);
		Integer[] str = new Integer[list_income.size() + list_pay.size()];
		if ((list_income.size() == 0) && (list_pay.size() == 0)) {
			ip_sum = 0;
		} else {
			for (Tb_pay tb_pay : list_pay) {// ����List���ͼ���
				// �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
				pay_sum += tb_pay.getMoney();
			}
			for (Tb_income tb_income : list_income) {// ����List���ͼ���
				// �����������Ϣ��ϳ�һ���ַ������洢���ַ����������Ӧλ��
				income_sum += tb_income.getMoney();
			}
			ip_sum = income_sum - pay_sum;
		}
		String ip_sum_str;
		if (Math.abs(ip_sum) > 100000000) {
			ip_sum_str = Integer.toString(ip_sum / 100000000) + "��";
		} else if (Math.abs(ip_sum) > 10000000) {
			ip_sum_str = Integer.toString(ip_sum / 10000000) + "ǧ��";
		} else if (Math.abs(ip_sum) > 1000000) {
			ip_sum_str = Integer.toString(ip_sum / 1000000) + "����";
		} else if (Math.abs(ip_sum) > 100000) {
			ip_sum_str = Integer.toString(ip_sum / 100000) + "ʮ��";
		} else if (Math.abs(ip_sum) > 10000) {
			ip_sum_str = Integer.toString(ip_sum / 10000) + "��";
		} else {
			ip_sum_str = Integer.toString(ip_sum);
		}

		// ip_sum = -2220;
		// budget = 3000;
		float d = budget / 3;
		if (Math.abs(ip_sum) > budget) {
			if (ip_sum < 0) {
				circle1 = -60;
				circle2 = -60;
				circle3 = -60;
			} else {
				circle1 = 60;
				circle2 = 60;
				circle3 = 60;
			}
		} else if (Math.abs(ip_sum) > budget * 2 / 3) {
			if (ip_sum < 0) {
				circle1 = -60;
				circle2 = -60;
				circle3 = -(int) ((Math.abs(ip_sum) - 2 * d) * 60 / d);
			} else {
				circle1 = 60;
				circle2 = 60;
				circle3 = (int) ((ip_sum - 2 * d) * 60 / d);
			}
		} else if (Math.abs(ip_sum) > d) {
			if (ip_sum < 0) {
				circle1 = -60;
				circle2 = -(int) ((Math.abs(ip_sum) - d) * 60 / d);
			} else {
				circle1 = 60;
				circle2 = (int) ((ip_sum - d) * 60 / d);
			}
			circle3 = 0;
		} else {
			if (ip_sum < 0) {
				circle1 = -(int) (Math.abs(ip_sum) * 60 / d);
			} else {
				circle1 = (int) (ip_sum * 60 / d);
			}
			circle2 = 0;
			circle3 = 0;
		}
		return defaultMonth + "|" + ip_sum_str + "|" + circle1 + "|" + circle2
				+ "|" + circle3;
	}
}
