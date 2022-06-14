package me.afoolslove.metalmaxre.editors.data;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据值
 * <p>
 * 用于：商品价格、攻击力、防御力等
 *
 * @author AFoolLove
 */
public interface IDataValueEditor extends IRomEditor {

    /**
     * @return 所有数据值
     */
    @NotNull
    Map<Integer, Number> getValues();

    default int get1ByteMaxCount() {
        return 0x47;
    }

    default int get2ByteMaxCount() {
        return 0x65;
    }

    default int get3ByteMaxCount() {
        return 0x3D;
    }

    DataAddress get1ByteAddress();

    DataAddress get2ByteAddress();

    DataAddress get3ByteAddress();

//    /**
//     * @return 该值的所有key
//     */
//    default Map<Integer, Number> getKeys(@Range(from = 0x00, to = 0xFFFFFF) int value) {
//        return getValues().entrySet().parallelStream()
//                .filter(entry -> entry.getValue() == value)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
//    }

    /**
     * @return 1Byte的数据
     */
    default Map<Integer, Number> get1ByteValues() {
        return getValues().entrySet().parallelStream()
                .filter(v -> v.getKey() < get1ByteMaxCount())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * @return 2Byte的数据
     */
    default Map<Integer, Number> get2ByteValues() {
        return getValues().entrySet().parallelStream()
                .filter(v -> v.getKey() >= get1ByteMaxCount() && v.getKey() < (get1ByteMaxCount() + get2ByteMaxCount()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * @return 3Byte的数据
     */
    default Map<Integer, Number> get3ByteValues() {
        return getValues().entrySet().parallelStream()
                .filter(v -> v.getKey() >= (get1ByteMaxCount() + get2ByteMaxCount()) && v.getKey() < (get1ByteMaxCount() + get2ByteMaxCount() + get3ByteMaxCount()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

//    private Map<Number, Integer> getFormByteLength(@Range(from = 0x01, to = 0x03) int length) {
//        if (length != 0x01 && length != 0x02) {
//            return new HashMap<>(getValues());
//        }
//        return getValues().entrySet().parallelStream()
//                .filter(entry -> switch (length) {
//                    case 1 -> entry.getValue() <= 0x0000FF;
//                    case 2 -> entry.getValue() <= 0x00FFFF;
////                    case 3 -> entry.getValue() <= 0xFFFFFF;
//                    default -> true;
//                })
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
}
