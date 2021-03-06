/*
 * Copyright 2020 Wultra s.r.o.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.wultra.security.pqc.sike.param;

import com.wultra.security.pqc.sike.math.api.*;
import com.wultra.security.pqc.sike.math.optimized.Fp2PointProjective;
import com.wultra.security.pqc.sike.math.optimized.IsogenyProjective;
import com.wultra.security.pqc.sike.math.optimized.MontgomeryProjective;
import com.wultra.security.pqc.sike.math.optimized.fp.Fp2ElementFactoryOpti;
import com.wultra.security.pqc.sike.math.optimized.fp.Fp2ElementOpti;
import com.wultra.security.pqc.sike.math.optimized.fp.FpElementOpti;
import com.wultra.security.pqc.sike.math.reference.Fp2PointAffine;
import com.wultra.security.pqc.sike.math.reference.IsogenyAffine;
import com.wultra.security.pqc.sike.math.reference.MontgomeryAffine;
import com.wultra.security.pqc.sike.math.reference.fp.Fp2ElementFactoryRef;
import com.wultra.security.pqc.sike.math.reference.fp.Fp2ElementRef;
import com.wultra.security.pqc.sike.math.reference.fp.FpElementRef;
import com.wultra.security.pqc.sike.model.ImplementationType;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.Objects;

/**
 * SIKE parameters for variant SIKEp751.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
public class SikeParamP751 implements SikeParam {

    private final ImplementationType implementationType;
    private final Fp2ElementFactory fp2ElementFactory;
    private final Montgomery montgomery;
    private final Isogeny isogeny;

    private String name;

    // Basic Montgomery curve parameters
    private Fp2Element a;
    private Fp2Element b;
    private int eA;
    private int eB;
    private BigInteger ordA;
    private BigInteger ordB;
    private int bitsA;
    private int bitsB;

    // Field prime and public points
    private BigInteger prime;
    private Fp2Point pA;
    private Fp2Point qA;
    private Fp2Point rA;
    private Fp2Point pB;
    private Fp2Point qB;
    private Fp2Point rB;

    // Sizes of generated bytes
    private int cryptoBytes;
    private int messageBytes;

    // Configuration of isogeny computation optimizations
    private int treeRowsA;
    private int treeRowsB;
    private int treePointsA;
    private int treePointsB;
    private int[] strategyA;
    private int[] strategyB;

    /**
     * Constructor of SIKE parameters.
     * @param implementationType Implementation type.
     */
    public SikeParamP751(ImplementationType implementationType) {
        this.implementationType = implementationType;
        if (implementationType == ImplementationType.REFERENCE) {
            fp2ElementFactory = new Fp2ElementFactoryRef(this);
            montgomery = new MontgomeryAffine();
            isogeny = new IsogenyAffine();
        } else if (implementationType == ImplementationType.OPTIMIZED) {
            fp2ElementFactory = new Fp2ElementFactoryOpti(this);
            montgomery = new MontgomeryProjective();
            isogeny = new IsogenyProjective();
        } else {
            throw new InvalidParameterException("Unsupported implementation type: " + implementationType);
        }
        init();
    }

    @Override
    public ImplementationType getImplementationType() {
        return implementationType;
    }

    @Override
    public Fp2ElementFactory getFp2ElementFactory() {
        return fp2ElementFactory;
    }

    @Override
    public Montgomery getMontgomery() {
        return montgomery;
    }

    @Override
    public Isogeny getIsogeny() {
        return isogeny;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Fp2Element getA() {
        return a;
    }

    @Override
    public Fp2Element getB() {
        return b;
    }

    @Override
    public int getEA() {
        return eA;
    }

    @Override
    public int getEB() {
        return eB;
    }

    @Override
    public BigInteger getOrdA() {
        return ordA;
    }

    @Override
    public BigInteger getOrdB() {
        return ordB;
    }

    @Override
    public int getBitsA() {
        return bitsA;
    }

    @Override
    public int getBitsB() {
        return bitsB;
    }

    @Override
    public byte getMaskA() {
        return 0x0F;
    }

    @Override
    public byte getMaskB() {
        return 0x03;
    }

    @Override
    public BigInteger getPrime() {
        return prime;
    }

    @Override
    public Fp2Point getPA() {
        return pA;
    }

    @Override
    public Fp2Point getQA() {
        return qA;
    }

    @Override
    public Fp2Point getRA() {
        return rA;
    }

    @Override
    public Fp2Point getPB() {
        return pB;
    }

    @Override
    public Fp2Point getQB() {
        return qB;
    }

    @Override
    public Fp2Point getRB() {
        return rB;
    }

    @Override
    public int getCryptoBytes() {
        return cryptoBytes;
    }

    @Override
    public int getMessageBytes() {
        return messageBytes;
    }

    @Override
    public int getTreeRowsA() {
        return treeRowsA;
    }

    @Override
    public int getTreeRowsB() {
        return treeRowsB;
    }

    @Override
    public int getTreePointsA() {
        return treePointsA;
    }

    @Override
    public int getTreePointsB() {
        return treePointsB;
    }

    @Override
    public int[] getStrategyA() {
        return strategyA;
    }

    @Override
    public int[] getStrategyB() {
        return strategyB;
    }

    @Override
    public int getFpWords() {
        return 12;
    }

    @Override
    public int getZeroWords() {
        return 5;
    }

    private final FpElementOpti p = new FpElementOpti(this, new long[]{
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("EEAFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("E3EC968549F878A8", 16),
            Long.parseUnsignedLong("DA959B1A13F7CC76", 16),
            Long.parseUnsignedLong("084E9867D6EBE876", 16),
            Long.parseUnsignedLong("8562B5045CB25748", 16),
            Long.parseUnsignedLong("0E12909F97BADC66", 16),
            Long.parseUnsignedLong("00006FE5D541F71C", 16)
    });

    @Override
    public FpElementOpti getP() {
        return p;
    }

    private final FpElementOpti p1 = new FpElementOpti(this, new long[]{
            Long.parseUnsignedLong("0000000000000000", 16),
            Long.parseUnsignedLong("0000000000000000", 16),
            Long.parseUnsignedLong("0000000000000000", 16),
            Long.parseUnsignedLong("0000000000000000", 16),
            Long.parseUnsignedLong("0000000000000000", 16),
            Long.parseUnsignedLong("EEB0000000000000", 16),
            Long.parseUnsignedLong("E3EC968549F878A8", 16),
            Long.parseUnsignedLong("DA959B1A13F7CC76", 16),
            Long.parseUnsignedLong("084E9867D6EBE876", 16),
            Long.parseUnsignedLong("8562B5045CB25748", 16),
            Long.parseUnsignedLong("0E12909F97BADC66", 16),
            Long.parseUnsignedLong("00006FE5D541F71C", 16)
    });

    @Override
    public FpElementOpti getP1() {
        return p1;
    }

    private final FpElementOpti px2 = new FpElementOpti(this, new long[]{
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFE", 16),
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("FFFFFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("DD5FFFFFFFFFFFFF", 16),
            Long.parseUnsignedLong("C7D92D0A93F0F151", 16),
            Long.parseUnsignedLong("B52B363427EF98ED", 16),
            Long.parseUnsignedLong("109D30CFADD7D0ED", 16),
            Long.parseUnsignedLong("0AC56A08B964AE90", 16),
            Long.parseUnsignedLong("1C25213F2F75B8CD", 16),
            Long.parseUnsignedLong("0000DFCBAA83EE38", 16)
    });

    @Override
    public FpElementOpti getPx2() {
        return px2;
    }

    private final FpElementOpti pr2 = new FpElementOpti(this, new long[]{
            Long.parseUnsignedLong("233046449DAD4058", 16),
            Long.parseUnsignedLong("DB010161A696452A", 16),
            Long.parseUnsignedLong("5E36941472E3FD8E", 16),
            Long.parseUnsignedLong("F40BFE2082A2E706", 16),
            Long.parseUnsignedLong("4932CCA8904F8751", 16),
            Long.parseUnsignedLong("1F735F1F1EE7FC81", 16),
            Long.parseUnsignedLong("A24F4D80C1048E18", 16),
            Long.parseUnsignedLong("B56C383CCDB607C5", 16),
            Long.parseUnsignedLong("441DD47B735F9C90", 16),
            Long.parseUnsignedLong("5673ED2C6A6AC82A", 16),
            Long.parseUnsignedLong("06C905261132294B", 16),
            Long.parseUnsignedLong("000041AD830F1F35", 16)
    });

    @Override
    public FpElementOpti getPR2() {
        return pr2;
    }

    public int[] getPowStrategy() {
        return new int[]{5, 7, 6, 2, 10, 4, 6, 9, 8, 5, 9, 4, 7, 5, 5, 4, 8, 3, 9, 5, 5, 4, 10, 4, 6, 6, 6, 5, 8, 9, 3, 4, 9, 4, 5, 6, 6, 2, 9, 4, 5, 5, 5, 7, 7, 9, 4, 6, 4, 8, 5, 8, 6, 6, 2, 9, 7, 4, 8, 8, 8, 4, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2};
    }

    public int[] getMulStrategy() {
        return new int[]{15, 11, 10, 0, 15, 3, 3, 3, 4, 4, 9, 7, 11, 11, 5, 3, 12, 2, 10, 8, 5, 2, 8, 3, 5, 4, 11, 4, 0, 9, 2, 1, 12, 7, 5, 14, 15, 0, 14, 5, 6, 4, 5, 13, 6, 9, 7, 15, 1, 14, 11, 15, 12, 5, 0, 10, 9, 7, 7, 10, 14, 6, 11, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 1};
    }

    public int getInitialMul() {
        return 13;
    }

    /**
     * Initialize variant SIKEp610.
     */
    private void init() {
        // EA = 372, EB = 239
        int FIELD_PRIME_PARAM_EA = 372;
        int FIELD_PRIME_PARAM_EB = 239;
        this.prime = new BigInteger("6FE5D541F71C0E12909F97BADC668562B5045CB25748084E9867D6EBE876DA959B1A13F7CC76E3EC968549F878A8EEAFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);

        // A = 6, B = 1
        Fp2Element PUBLIC_PARAM_ELEMENT_A = fp2ElementFactory.generate(new BigInteger("6"));
        Fp2Element PUBLIC_PARAM_ELEMENT_B = fp2ElementFactory.one();

        Fp2Point PUBLIC_POINT_PA, PUBLIC_POINT_QA, PUBLIC_POINT_RA;
        Fp2Point PUBLIC_POINT_PB, PUBLIC_POINT_QB, PUBLIC_POINT_RB;

        if (implementationType == ImplementationType.REFERENCE) {
            // Public points PA and QA
            FpElementRef PUBLIC_POINT_PA_X0 = new FpElementRef(this, new BigInteger("4514F8CC94B140F24874F8B87281FA6004CA5B3637C68AC0C0BDB29838051F385FBBCC300BBB24BFBBF6710D7DC8B29ACB81E429BD1BD5629AD0ECAD7C90622F6BB801D0337EE6BC78A7F12FDCB09DECFAE8BFD643C89C3BAC1D87F8B6FA", 16));
            FpElementRef PUBLIC_POINT_PA_X1 = new FpElementRef(this, new BigInteger("158ABF500B5914B3A96CED5FDB37D6DD925F2D6E4F7FEA3CC16E1085754077737EA6F8CC74938D971DA289DCF2435BCAC1897D2627693F9BB167DC01BE34AC494C60B8A0F65A28D7A31EA0D54640653A8099CE5A84E4F0168D818AF02041", 16));
            FpElementRef PUBLIC_POINT_PA_Y0 = new FpElementRef(this, new BigInteger("BF6E4E7A28E9A6EF66A2F1614AE2A2B5A583C9F2DC6C83F84E2D9E6577F9E22B991D58FB2F89666DC1D40A2C0A3AB876CF8DA8878F12325BF8B0CF92E45AE00627041C891BC96FFBB874FC587E4342F78098258DF2E10A5708A70A0D5A8", 16));
            FpElementRef PUBLIC_POINT_PA_Y1 = new FpElementRef(this, new BigInteger("1502FB44178D1DF80A53858519CBCF233FE387905BC8F9E4138703C6DB7C82302FBFB7E97153F6001FE9102D2597AC2B300A1C669D1A2803F8D05BA3B1F2ACBF27BC1A127B4A553916D62004FD21633C5AEAAB74833853B4C5C42EB71F7E", 16));
            FpElementRef PUBLIC_POINT_QA_X0 = new FpElementRef(this, new BigInteger("1723D2BFA01A78BF4E39E3A333F8A7E0B415A17F208D3419E7591D59D8ABDB7EE6D2B2DFCB21AC29A40F837983C0F057FD041AD93237704F1597D87F074F682961A38B5489D1019924F8A0EF5E4F1B2E64A7BA536E219F5090F76276290E", 16));
            FpElementRef PUBLIC_POINT_QA_X1 = new FpElementRef(this, new BigInteger("2569D7EAFB6C60B244EF49E05B5E23F73C4F44169A7E02405E90CEB680CB0756054AC0E3DCE95E2950334262CC973235C2F87D89500BCD465B078BD0DEBDF322A2F86AEDFDCFEE65C09377EFBA0C5384DD837BEDB710209FBC8DDB8C35C7", 16));
            FpElementRef PUBLIC_POINT_QA_Y0 = new FpElementRef(this, new BigInteger("35B82D1BD2BA608B42794C4820C56A3D8BBAD28380B8D85A1910E2609A61F7BC0BCA8ED8EF883E7E98C744A0AC85D2893738521B62EB23D1983D2EDCF2AB437108DC048AA853FF9BC791224B121E8FDF1EA5F617E6ED5898663DDED49154", 16));
            FpElementRef PUBLIC_POINT_QA_Y1 = new FpElementRef(this, new BigInteger("F22306A6963907F16AA38F89C672A4054DB5FD1D26598A3140EA204B10094AE64093142AEB056942494D216A74ED9F51FFC9272D1772151013334EC570B532DB0C083CF39867F63D191029033F942E977B85F69EC738B4C26D3B72E2821", 16));
            FpElementRef PUBLIC_POINT_RA_X0 = new FpElementRef(this, new BigInteger("6066E07F3C0D964E8BC963519FAC8397DF477AEA9A067F3BE343BC53C883AF29CCF008E5A30719A29357A8C33EB3600CD078AF1C40ED5792763A4D213EBDE44CC623195C387E0201E7231C529A15AF5AB743EE9E7C9C37AF3051167525BB", 16));
            FpElementRef PUBLIC_POINT_RA_X1 = new FpElementRef(this, new BigInteger("50E30C2C06494249BC4A144EB5F31212BD05A2AF0CB3064C322FC3604FC5F5FE3A08FB3A02B05A48557E15C992254FFC8910B72B8E1328B4893CDCFBFC003878881CE390D909E39F83C5006E0AE979587775443483D13C65B107FADA5165", 16));
            // The Y points are not defined for R point, only x coordinates are used in optimized version
            FpElementRef PUBLIC_POINT_RA_Y0 = new FpElementRef(this, new BigInteger("0", 16));
            FpElementRef PUBLIC_POINT_RA_Y1 = new FpElementRef(this, new BigInteger("0", 16));
            PUBLIC_POINT_PA = new Fp2PointAffine(new Fp2ElementRef(this, PUBLIC_POINT_PA_X0, PUBLIC_POINT_PA_X1), new Fp2ElementRef(this, PUBLIC_POINT_PA_Y0, PUBLIC_POINT_PA_Y1));
            PUBLIC_POINT_QA = new Fp2PointAffine(new Fp2ElementRef(this, PUBLIC_POINT_QA_X0, PUBLIC_POINT_QA_X1), new Fp2ElementRef(this, PUBLIC_POINT_QA_Y0, PUBLIC_POINT_QA_Y1));
            PUBLIC_POINT_RA = new Fp2PointAffine(new Fp2ElementRef(this, PUBLIC_POINT_RA_X0, PUBLIC_POINT_RA_X1), new Fp2ElementRef(this, PUBLIC_POINT_RA_Y0, PUBLIC_POINT_RA_Y1));

            // Public points PB, QB and RB
            FpElementRef PUBLIC_POINT_PB_X0 = new FpElementRef(this, new BigInteger("605D4697A245C394B98024A5554746DC12FF56D0C6F15D2F48123B6D9C498EEE98E8F7CD6E216E2F1FF7CE0C969CCA29CAA2FAA57174EF985AC0A504260018760E9FDF67467E20C13982FF5B49B8BEAB05F6023AF873F827400E453432FE", 16));
            FpElementRef PUBLIC_POINT_PB_X1 = new FpElementRef(this, new BigInteger("0", 16));
            FpElementRef PUBLIC_POINT_PB_Y0 = new FpElementRef(this, new BigInteger("5634690BFC14C45E2FAA930D62589855E5BDD1435CFFBDF609628FD043B4BF295BB35F7B6D37836F2C59A27BB61ED0FF57FF8093FE6B712133D26502F17CB0D46CDC8CF9BA7664EA2B6C1672A8CA2FF1CE313FEEEF4199FC7F14FE720617", 16));
            FpElementRef PUBLIC_POINT_PB_Y1 = new FpElementRef(this, new BigInteger("0", 16));
            FpElementRef PUBLIC_POINT_QB_X0 = new FpElementRef(this, new BigInteger("5BF9544781803CBD7E0EA8B96D934C5CBCA970F9CC327A0A7E4DAD931EC29BAA8A854B8A9FDE5409AF96C5426FA375D99C68E9AE714172D7F04502D45307FA4839F39A28338BBAFD54A461A535408367D5132E6AA0D3DA6973360F8CD0F1", 16));
            FpElementRef PUBLIC_POINT_QB_X1 = new FpElementRef(this, new BigInteger("0", 16));
            FpElementRef PUBLIC_POINT_QB_Y0 = new FpElementRef(this, new BigInteger("0", 16));
            FpElementRef PUBLIC_POINT_QB_Y1 = new FpElementRef(this, new BigInteger("3351F421FC158472AC2DD8B4DABB5B599456748A5BCC4449398F05ED1AD1414B4EEBBB70FB91383474B712EA4B5BF096092CDDD57C0A090B041022064C3A8DD3D890E7B5AC34A24CEF507955F027CC4CECFDB67739CE89F31FDC5FE43243", 16));
            FpElementRef PUBLIC_POINT_RB_X0 = new FpElementRef(this, new BigInteger("55E5124A05D4809585F67FE9EA1F02A06CD411F38588BB631BF789C3F98D1C3325843BB53D9B011D8BD1F682C0E4D8A5E723364364E40DAD1B7A476716AC7D1BA705CCDD680BFD4FE4739CC21A9A59ED544B82566BF633E8950186A79FE3", 16));
            FpElementRef PUBLIC_POINT_RB_X1 = new FpElementRef(this, new BigInteger("5AC57EAFD6CC7569E8B53A148721953262C5B404C143380ADCC184B6C21F0CAFE095B7E9C79CA88791F9A72F1B2F3121829B2622515B694A16875ED637F421B539E66F2FEF1CE8DCEFC8AEA608055E9C44077266AB64611BF851BA06C821", 16));
            // The Y points are not defined for R point, only x coordinates are used in optimized version
            FpElementRef PUBLIC_POINT_RB_Y0 = new FpElementRef(this, new BigInteger("0", 16));
            FpElementRef PUBLIC_POINT_RB_Y1 = new FpElementRef(this, new BigInteger("0", 16));
            PUBLIC_POINT_PB = new Fp2PointAffine(new Fp2ElementRef(this, PUBLIC_POINT_PB_X0, PUBLIC_POINT_PB_X1), new Fp2ElementRef(this, PUBLIC_POINT_PB_Y0, PUBLIC_POINT_PB_Y1));
            PUBLIC_POINT_QB = new Fp2PointAffine(new Fp2ElementRef(this, PUBLIC_POINT_QB_X0, PUBLIC_POINT_QB_X1), new Fp2ElementRef(this, PUBLIC_POINT_QB_Y0, PUBLIC_POINT_QB_Y1));
            PUBLIC_POINT_RB = new Fp2PointAffine(new Fp2ElementRef(this, PUBLIC_POINT_RB_X0, PUBLIC_POINT_RB_X1), new Fp2ElementRef(this, PUBLIC_POINT_RB_Y0, PUBLIC_POINT_RB_Y1));
        } else {
            FpElement PUBLIC_POINT_PA_X0 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("884F46B74000BAA8", 16),
                    Long.parseUnsignedLong("BA52630F939DEC20", 16),
                    Long.parseUnsignedLong("C16FB97BA714A04D", 16),
                    Long.parseUnsignedLong("082536745B1AB3DB", 16),
                    Long.parseUnsignedLong("1117157F446F9E82", 16),
                    Long.parseUnsignedLong("D2F27D621A018490", 16),
                    Long.parseUnsignedLong("6B24AB523D544BCD", 16),
                    Long.parseUnsignedLong("9307D6AA2EA85C94", 16),
                    Long.parseUnsignedLong("E1A096729528F20F", 16),
                    Long.parseUnsignedLong("896446F868F3255C", 16),
                    Long.parseUnsignedLong("2401D996B1BFF8A5", 16),
                    Long.parseUnsignedLong("00000EF8786A5C0A", 16)
            });
            FpElement PUBLIC_POINT_PA_X1 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("AEB78B3B96F59394", 16),
                    Long.parseUnsignedLong("AB26681E29C90B74", 16),
                    Long.parseUnsignedLong("E520AC30FDC4ACF1", 16),
                    Long.parseUnsignedLong("870AAAE3A4B8111B", 16),
                    Long.parseUnsignedLong("F875BDB738D64EFF", 16),
                    Long.parseUnsignedLong("50109A7ECD7ED6BC", 16),
                    Long.parseUnsignedLong("4CC64848FF0C56FB", 16),
                    Long.parseUnsignedLong("E617CB6C519102C9", 16),
                    Long.parseUnsignedLong("9C74B3835921E609", 16),
                    Long.parseUnsignedLong("C91DDAE4A35A7146", 16),
                    Long.parseUnsignedLong("7FC82A155C1B9129", 16),
                    Long.parseUnsignedLong("0000214FA6B980B3", 16)
            });
            FpElement PUBLIC_POINT_QA_X0 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("0F93CC38680A8CA9", 16),
                    Long.parseUnsignedLong("762E733822E7FED7", 16),
                    Long.parseUnsignedLong("E549F005AC0ADB67", 16),
                    Long.parseUnsignedLong("94A71FDD2C43A4ED", 16),
                    Long.parseUnsignedLong("D48645C2B04721C5", 16),
                    Long.parseUnsignedLong("432DA1FE4D4CA4DC", 16),
                    Long.parseUnsignedLong("BC99655FAA7A80E8", 16),
                    Long.parseUnsignedLong("B2C6D502BCFD4823", 16),
                    Long.parseUnsignedLong("EE92F40CA2EC8BDB", 16),
                    Long.parseUnsignedLong("7B074132EFB6D16C", 16),
                    Long.parseUnsignedLong("3340B46FA38A7633", 16),
                    Long.parseUnsignedLong("0000215749657F6C", 16)
            });
            FpElement PUBLIC_POINT_QA_X1 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("ECFF375BF3079F4C", 16),
                    Long.parseUnsignedLong("FBFE74B043E80EF3", 16),
                    Long.parseUnsignedLong("17376CBE3C5C7AD1", 16),
                    Long.parseUnsignedLong("C06327A7E29CDBF2", 16),
                    Long.parseUnsignedLong("2111649C438BF3D4", 16),
                    Long.parseUnsignedLong("C1F9298261BA2E97", 16),
                    Long.parseUnsignedLong("1F9FECE869CFD1C2", 16),
                    Long.parseUnsignedLong("01A39B4FC9346D62", 16),
                    Long.parseUnsignedLong("147CD1D3E82A3C9F", 16),
                    Long.parseUnsignedLong("DE84E9D249E533EE", 16),
                    Long.parseUnsignedLong("1C48A5ADFB7C578D", 16),
                    Long.parseUnsignedLong("000061ACA0B82E1D", 16)
            });
            FpElement PUBLIC_POINT_RA_X0 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("1600C525D41059F1", 16),
                    Long.parseUnsignedLong("A596899A0A1D83F7", 16),
                    Long.parseUnsignedLong("6BFDEED6D2B23F35", 16),
                    Long.parseUnsignedLong("5C7E707270C23910", 16),
                    Long.parseUnsignedLong("276CA1A4E8369411", 16),
                    Long.parseUnsignedLong("B193651A602925A0", 16),
                    Long.parseUnsignedLong("243D239F1CA1F04A", 16),
                    Long.parseUnsignedLong("543DC6DA457860AD", 16),
                    Long.parseUnsignedLong("CDA590F325181DE9", 16),
                    Long.parseUnsignedLong("D3AB7ACFDA80B395", 16),
                    Long.parseUnsignedLong("6C97468580FDDF7B", 16),
                    Long.parseUnsignedLong("0000352A3E5C4C77", 16)
            });
            FpElement PUBLIC_POINT_RA_X1 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("9B794F9FD1CC3EE8", 16),
                    Long.parseUnsignedLong("DB32E40A9B2FD23E", 16),
                    Long.parseUnsignedLong("26192A2542E42B67", 16),
                    Long.parseUnsignedLong("A18E94FCA045BCE7", 16),
                    Long.parseUnsignedLong("96DC1BC38E7CDA2D", 16),
                    Long.parseUnsignedLong("9A1D91B752487DE2", 16),
                    Long.parseUnsignedLong("CC63763987436DA3", 16),
                    Long.parseUnsignedLong("1316717AACCC551D", 16),
                    Long.parseUnsignedLong("C4C368A4632AFE72", 16),
                    Long.parseUnsignedLong("4B6EA85C9CCD5710", 16),
                    Long.parseUnsignedLong("7A12CAD582C7BC9A", 16),
                    Long.parseUnsignedLong("00001C7E240149BF", 16)
            });
            PUBLIC_POINT_PA = new Fp2PointProjective(new Fp2ElementOpti(this, PUBLIC_POINT_PA_X0, PUBLIC_POINT_PA_X1), fp2ElementFactory.one());
            PUBLIC_POINT_QA = new Fp2PointProjective(new Fp2ElementOpti(this, PUBLIC_POINT_QA_X0, PUBLIC_POINT_QA_X1), fp2ElementFactory.one());
            PUBLIC_POINT_RA = new Fp2PointProjective(new Fp2ElementOpti(this, PUBLIC_POINT_RA_X0, PUBLIC_POINT_RA_X1), fp2ElementFactory.one());
            FpElement PUBLIC_POINT_PB_X0 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("85691AAF4015F88C", 16),
                    Long.parseUnsignedLong("7478C5B8C36E9631", 16),
                    Long.parseUnsignedLong("7EF2A185DE4DD6E2", 16),
                    Long.parseUnsignedLong("943BBEE46BEB9DC7", 16),
                    Long.parseUnsignedLong("1A3EC62798792D22", 16),
                    Long.parseUnsignedLong("791BC4B084B31D69", 16),
                    Long.parseUnsignedLong("03DBE6522CEA17C4", 16),
                    Long.parseUnsignedLong("04749AA65D665D83", 16),
                    Long.parseUnsignedLong("3D52B5C45EF450F3", 16),
                    Long.parseUnsignedLong("0B4219848E36947D", 16),
                    Long.parseUnsignedLong("A4CF7070466BDE27", 16),
                    Long.parseUnsignedLong("0000334B1FA6D193", 16)
            });
            FpElement PUBLIC_POINT_PB_X1 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16)
            });
            FpElement PUBLIC_POINT_QB_X0 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("8E7CB3FA53211340", 16),
                    Long.parseUnsignedLong("D67CE54F7A05EEE0", 16),
                    Long.parseUnsignedLong("FDDC2C8BCE46FC38", 16),
                    Long.parseUnsignedLong("08587FAE3110DF1E", 16),
                    Long.parseUnsignedLong("D6B8246FA22B058B", 16),
                    Long.parseUnsignedLong("4DAC3ACC905A5DBD", 16),
                    Long.parseUnsignedLong("51D0BF2FADCED3E8", 16),
                    Long.parseUnsignedLong("E5A2406DF6484425", 16),
                    Long.parseUnsignedLong("907F177584F671B8", 16),
                    Long.parseUnsignedLong("4738A2FFCCED051C", 16),
                    Long.parseUnsignedLong("2B0067B4177E4853", 16),
                    Long.parseUnsignedLong("00002806AC948D3D", 16)
            });
            FpElement PUBLIC_POINT_QB_X1 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16),
                    Long.parseUnsignedLong("0000000000000000", 16)
            });
            FpElement PUBLIC_POINT_RB_X0 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("B56457016D1D6D1C", 16),
                    Long.parseUnsignedLong("03DECCB38F39C491", 16),
                    Long.parseUnsignedLong("DFB910AC8A559452", 16),
                    Long.parseUnsignedLong("A9D0F17D1FF24883", 16),
                    Long.parseUnsignedLong("8562BBAF515C248C", 16),
                    Long.parseUnsignedLong("249B2A6DDB1CB67D", 16),
                    Long.parseUnsignedLong("3131AF96FB46835C", 16),
                    Long.parseUnsignedLong("E10258398480C3E1", 16),
                    Long.parseUnsignedLong("EAB5E2B872D4FAB1", 16),
                    Long.parseUnsignedLong("B71E63875FAEB1DF", 16),
                    Long.parseUnsignedLong("F8384D4F13757CF6", 16),
                    Long.parseUnsignedLong("0000361EC9B09912", 16)
            });
            FpElement PUBLIC_POINT_RB_X1 = new FpElementOpti(this,  new long[]{
                    Long.parseUnsignedLong("58C967899ED16EF4", 16),
                    Long.parseUnsignedLong("81998376DC622A4B", 16),
                    Long.parseUnsignedLong("3D1C1DCFE0B12681", 16),
                    Long.parseUnsignedLong("9347DEBB953E1730", 16),
                    Long.parseUnsignedLong("9ABB344D3A82C2D7", 16),
                    Long.parseUnsignedLong("E4881BD2820552B2", 16),
                    Long.parseUnsignedLong("0037247923D90266", 16),
                    Long.parseUnsignedLong("2E3156EDB157E5A5", 16),
                    Long.parseUnsignedLong("F86A46A7506823F7", 16),
                    Long.parseUnsignedLong("8FE5523A7B7F1CFC", 16),
                    Long.parseUnsignedLong("FA3CFFA38372F67B", 16),
                    Long.parseUnsignedLong("0000692DCE85FFBD", 16)
            });
            PUBLIC_POINT_PB = new Fp2PointProjective(new Fp2ElementOpti(this, PUBLIC_POINT_PB_X0, PUBLIC_POINT_PB_X1), fp2ElementFactory.one());
            PUBLIC_POINT_QB = new Fp2PointProjective(new Fp2ElementOpti(this, PUBLIC_POINT_QB_X0, PUBLIC_POINT_QB_X1), fp2ElementFactory.one());
            PUBLIC_POINT_RB = new Fp2PointProjective(new Fp2ElementOpti(this, PUBLIC_POINT_RB_X0, PUBLIC_POINT_RB_X1), fp2ElementFactory.one());
        }

        this.name = "SIKEp751";
        this.a = PUBLIC_PARAM_ELEMENT_A;
        this.b = PUBLIC_PARAM_ELEMENT_B;
        this.eA = FIELD_PRIME_PARAM_EA;
        this.eB = FIELD_PRIME_PARAM_EB;
        this.ordA = new BigInteger("9619630419041620901435312524449124464130795720328478190417063819395928166869436184427311097384012607618805661696");
        this.ordB = new BigInteger("1076415339332851335838408278837787304900791017342367285006484190592481075231153579549080863047304729836926607724267");
        this.bitsA = 372;
        this.bitsB = 379;
        this.pA = PUBLIC_POINT_PA;
        this.qA = PUBLIC_POINT_QA;
        this.rA = PUBLIC_POINT_RA;
        this.pB = PUBLIC_POINT_PB;
        this.qB = PUBLIC_POINT_QB;
        this.rB = PUBLIC_POINT_RB;
        this.cryptoBytes = 32;
        this.messageBytes = 32;
        this.treeRowsA = 186;
        this.treeRowsB = 239;
        this.treePointsA = 8;
        this.treePointsB = 10;
        this.strategyA = new int[]{80, 48, 27, 15, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 7, 4, 2, 1, 1, 2, 1, 1, 3, 2, 1, 1, 1, 1, 12, 7, 4, 2, 1, 1, 2, 1, 1, 3, 2, 1, 1, 1, 1, 5, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1, 21, 12, 7, 4, 2, 1, 1, 2, 1, 1, 3, 2, 1, 1, 1, 1, 5, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1, 9, 5, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1, 4, 2, 1, 1, 1, 2, 1, 1, 33, 20, 12, 7, 4, 2, 1, 1, 2, 1, 1, 3, 2, 1, 1, 1, 1, 5, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1, 8, 5, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1, 4, 2, 1, 1, 2, 1, 1, 16, 8, 4, 2, 1, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1};
        this.strategyB = new int[]{112, 63, 32, 16, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 16, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 31, 16, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 15, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 7, 4, 2, 1, 1, 2, 1, 1, 3, 2, 1, 1, 1, 1, 49, 31, 16, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 15, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 7, 4, 2, 1, 1, 2, 1, 1, 3, 2, 1, 1, 1, 1, 21, 12, 8, 4, 2, 1, 1, 2, 1, 1, 4, 2, 1, 1, 2, 1, 1, 5, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1, 9, 5, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1, 4, 2, 1, 1, 1, 2, 1, 1};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SikeParamP751 that = (SikeParamP751) o;
        return implementationType == that.implementationType &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(implementationType, name);
    }
}
