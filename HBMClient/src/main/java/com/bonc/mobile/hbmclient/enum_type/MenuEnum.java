package com.bonc.mobile.hbmclient.enum_type;

import com.bonc.mobile.hbmclient.R;

/**
 * @author liweigao
 *
 */

public enum MenuEnum {
	JINGFENRIBAO(1, R.mipmap.icon_jingfenribao,
			R.mipmap.icon_jingfenribao_small), JINGZHENGLEI(13,
			R.mipmap.icon_jingzhenglei, R.mipmap.icon_jingzhenglei_small), SHOURULEI(
			14, R.mipmap.icon_shourulei, R.mipmap.icon_shourulei_small), YEWULIANGLEI(
			15, R.mipmap.icon_yewulianglei,
			R.mipmap.icon_yewulianglei_small), KAOHELEI(12,
			R.mipmap.icon_kaohelei, R.mipmap.icon_kaohelei_small), KEHULEI(
			16, R.mipmap.icon_kehulei, R.mipmap.icon_kehulei_small), ZHONGDUANLEI(
			250, R.mipmap.icon_zhongduan, R.mipmap.icon_zhongduan_small), MEIRIDENGLUTONGJI(
			50, R.mipmap.icon_denglutongji,
			R.mipmap.icon_denglutongji_small), ZHONGDUAN(2,
			R.mipmap.icon_zhongduan, R.mipmap.icon_zhongduan_small), LIULIANG(
			11000000, R.mipmap.icon_liuliang, R.mipmap.icon_liuliang_small), CUNLIANG(
			4, R.mipmap.icon_cunliang, R.mipmap.icon_cunliang_small), ZHENGQI(
			133, R.mipmap.icon_zhengqi, R.mipmap.icon_zhengqi_small), SHUJUYEWU(
			5, R.mipmap.icon_shujuyewu, R.mipmap.icon_shujuyewu_small), MIMACHUSHIHUA(
			51, R.mipmap.icon_mimachushihua,
			R.mipmap.icon_mimachushihua_small), GONGGAOCHAKAN(52,
			R.mipmap.icon_gonggaochakan, R.mipmap.icon_gonggaochakan_small), XIUGAIMIMA(
			53, R.mipmap.icon_xiugaimima, R.mipmap.icon_xiugaimima_small), YONGHUFASONGXINXICHAXUN(
			54, R.mipmap.icon_fasongxinxichaxun,
			R.mipmap.icon_fasongxinxichaxun_small), JIEKOUSHUJUJIANKONG(55,
			R.mipmap.icon_jiekoushujujiankong,
			R.mipmap.icon_jiekoushujujiankong_small), SHUJUJIANBAO(56,
			R.mipmap.icon_shujujianbao, R.mipmap.icon_shujujianbao_small), KUAIBAO(
			300, R.mipmap.icon_jiekoushujujiankong,
			R.mipmap.icon_jiekoushujujiankong_small), ZHENGQIKUAIBAO(301,
			R.mipmap.icon_zhengqi, R.mipmap.icon_zhengqi_small), ZHIBIAO4G(
			302, R.mipmap.icon_jiekoushujujiankong,
			R.mipmap.icon_jiekoushujujiankong_small), GAOLIULIANGQIANYI2G(
			11000011, R.mipmap.icon_2ggaoliuliangqianyi,
			R.mipmap.icon_2ggaoliuliangqianyi_small), GAOLIULIANGDIJILINGYINGXIAO(
			11000013, R.mipmap.icon_gaoliuliangdijilingyingxiao,
			R.mipmap.icon_gaoliuliangdijilingyingxiao_small), YIJILIANGXIANYINGXIAO(
			11000015, R.mipmap.icon_yijiliangxiangyingxiao,
			R.mipmap.icon_yijiliangxianyingxiao_small), YINGXIAOFENXI(
			12000087, R.mipmap.icon_zhengqi, R.mipmap.icon_zhengqi_small), QUDAOZHUANTI(
			305, R.mipmap.icon_qudaozhuanti,
			R.mipmap.icon_qudaozhuanti_small);

	private final int menuCode;
	private final int idIcon;
	private final int idIconSmall;

	private static final int default_icon = R.mipmap.icon_jingfenribao;
	private static final int default_icon_small = R.mipmap.icon_jingfenribao_small;

	private MenuEnum(int menucode, int id_icon, int id_icon_small) {
		this.menuCode = menucode;
		this.idIcon = id_icon;
		this.idIconSmall = id_icon_small;
	}

	public int getMenuCode() {
		return this.menuCode;
	}

	public int getIdIcon() {
		return this.idIcon;
	}

	public int getIdIconSmall() {
		return this.idIconSmall;
	}

	public static int getIdIcon(int menuCode) {
		for (MenuEnum me : MenuEnum.values()) {
			if (me.getMenuCode() == menuCode) {
				return me.getIdIcon();
			}
		}
		return default_icon;
	}

	public static int getIdIconSmall(int menuCode) {
		for (MenuEnum me : MenuEnum.values()) {
			if (me.getMenuCode() == menuCode) {
				return me.getIdIconSmall();
			}
		}
		return default_icon_small;
	}
}
