package com.ryx.swiper.devs.newland.common;

import java.math.BigDecimal;

import com.newland.mtype.module.common.emv.EmvControllerListener;
import com.newland.mtype.module.common.emv.EmvTransInfo;
import com.newland.mtype.module.common.swiper.SwipResult;

public interface TransferListener extends EmvControllerListener{
	public void onSwipMagneticCard(SwipResult swipRslt);
	public void onOpenCardreaderCanceled();
	//ME15
	public void onSwipMagneticCard(SwipResult swipRslt, BigDecimal amt);

	public void onIcCardStartSwiper();
	public void onRFCardSwiped(SwipResult result, EmvTransInfo emvTransInfo) throws Exception;
}
