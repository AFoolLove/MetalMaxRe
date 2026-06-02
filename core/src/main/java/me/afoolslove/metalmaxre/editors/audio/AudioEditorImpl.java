package me.afoolslove.metalmaxre.editors.audio;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AudioEditorImpl extends RomBufferWrapperAbstractEditor implements IAudioEditor {
    public static final String AUDIO_INDEX_ADDRESS = "audioIndexAddress";
    public static final String AUDIO_DATA_ADDRESS = "audioDataAddress";

    private final Map<Integer, AudioProperties> audioProperties = new HashMap<>();

    public AudioEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x3AEF0 - 0x10, 0x3AFE5 - 0x10),
                DataAddress.fromPRG(0x3AFF6 - 0x10, 0x3B2FB - 0x10)
        );
    }

    public AudioEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                           @NotNull DataAddress audioIndexAddress,
                           @NotNull DataAddress audioDataAddress) {
        super(metalMaxRe);
        putDataAddress(AUDIO_INDEX_ADDRESS, audioIndexAddress);
        putDataAddress(AUDIO_DATA_ADDRESS, audioDataAddress);
    }

    @Editor.Load
    public void onLoad() {
        Map<Integer, byte[]> audioData = new HashMap<>();
        loadIndexedData(AUDIO_INDEX_ADDRESS, AUDIO_DATA_ADDRESS, true, audioData, 0x7B);

        for (Map.Entry<Integer, byte[]> entry : audioData.entrySet()) {
            getAudioProperties().put(entry.getKey(), new AudioProperties(entry.getValue()));
        }
    }

    @Editor.Apply
    public void onApply() {
    }

    public Map<Integer, AudioProperties> getAudioProperties() {
        return this.audioProperties;
    }
}
