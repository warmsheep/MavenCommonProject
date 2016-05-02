package org.warmsheep.util.security.mac.impl;

import org.warmsheep.util.security.des.impl.DESede;
import org.warmsheep.util.security.exception.MacException;
import org.warmsheep.util.security.mac.AbstractMac;

public class EcardMac extends AbstractMac {
	public byte[] getMac(byte[] src, byte[] tak) throws MacException {
		try {
			if ((src == null) || (src.length == 0))
				throw new MacException("计算MAC的数据为空, src = " + src);
			if (tak == null)
				throw new MacException("TAK为空");

			src = getEightMultiplesData(src);
			int dataLen = src.length;
			int groupLen = dataLen / 8;
			byte[][] body = new byte[groupLen][8];
			int index = 0;
			for (int i = 0; i < groupLen; i++) {
				System.arraycopy(src, index, body[i], 0, 8);
				index += 8;
			}
			byte[] zero = new byte[8];
			DESede ed = DESede.newInstance16(tak);
			for (int i = 0; i < groupLen; i++) {
				zero = ed.encrypt(getExclusiveOR(body[i], zero));
			}
			byte[] result = new byte[4];
			System.arraycopy(zero, 0, result, 0, 4);
			zero = (byte[]) null;
			return result;
		} catch (Exception e) {
			System.err.println(e);
			throw new MacException("MAC计算有误," + e.getMessage());
		}
	}
}
