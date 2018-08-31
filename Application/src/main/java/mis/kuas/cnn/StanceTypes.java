package mis.kuas.cnn;

import com.example.android.stancerecognizer.R;

/**
 * Created by mingjia on 2016/10/6.
 */
public class StanceTypes {

    private static final StanceType[] STANCE_TYPES = {
            new StanceType("A0001", "站立", R.drawable.standing),
            new StanceType("A0002", "插腰", R.drawable.akimbo),
            new StanceType("A0003", "站著划手機", R.drawable.play_phone_sitting),
            new StanceType("A0004", "站著打電話", R.drawable.phone_standing),
            new StanceType("A0005", "舉手", R.drawable.raise_hand_standing),
            new StanceType("A2006", "揮手", R.drawable.swing_hand_sitting),
            new StanceType("A0007", "蹲著", R.drawable.ducking),
            new StanceType("A2008", "彎腰(x)", R.drawable.stand),
            new StanceType("A0009", "站著喝水", R.drawable.drinking_standing),
            new StanceType("B3001", "走路", R.drawable.wolking),
            new StanceType("B1002", "走著划手機", R.drawable.play_phone_walking),
            new StanceType("B1003", "走著打電話", R.drawable.phone_walking),
            new StanceType("B1004", "走著拿杯子(x)", R.drawable.stand),
            new StanceType("B3005", "跑步", R.drawable.running),
            new StanceType("B3006", "踏步", R.drawable.stepping),
            new StanceType("C3001", "用力跳", R.drawable.jumping),
            new StanceType("C3002", "小跳(x)", R.drawable.stand),
            new StanceType("C3003", "單腳跳(左)(x)", R.drawable.stand),
            new StanceType("C3004", "單腳跳(右)(x)", R.drawable.stand),
            new StanceType("C3005", "往前跳(x)", R.drawable.stand),
            new StanceType("D0001", "坐在椅上", R.drawable.sitting),
            new StanceType("D0002", "坐著打電話", R.drawable.phone_sitting),
            new StanceType("D0003", "坐著打電腦", R.drawable.computer),
            new StanceType("D0004", "趴在桌上(x)", R.drawable.stand),
            new StanceType("D0005", "翹左腳", R.drawable.sit_cross_left_legged),
            new StanceType("D0006", "翹右腳", R.drawable.sit_cross_right_legged),
            new StanceType("D0007", "盤坐", R.drawable.sit_cross_legged),
            new StanceType("D2008", "吃飯", R.drawable.eating),
            new StanceType("D0009", "跪坐", R.drawable.kneeling),
            new StanceType("E0001", "躺著", R.drawable.lying),
            new StanceType("E0002", "趴著", R.drawable.tummy),
            new StanceType("E0003", "側躺(左)", R.drawable.lying_left),
            new StanceType("E0004", "側躺(右)", R.drawable.lying_right),
    };

    public static StanceType[] getTypesArray() {
        return STANCE_TYPES;
    }

    public static StanceType getStanceTypeByCode(String code) {
        for (StanceType type : STANCE_TYPES) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

}
