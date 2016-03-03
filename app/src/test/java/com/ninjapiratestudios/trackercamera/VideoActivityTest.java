package com.ninjapiratestudios.trackercamera;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit tests for VideoActivity.java.
 * @author John Qualls
 * @version 3/2/2016
 */
@PrepareForTest(VideoActivity.class)
@RunWith(PowerMockRunner.class)
public class VideoActivityTest extends BaseTest {
    private VideoActivity videoActivity; // SUT
    @Mock
    private CameraRecorder cameraRecorder;

    @Before
    public void setup() {
        videoActivity = new VideoActivity();
    }

    /**
     *
     */
    @Test
    public void tempTest() {
        // Run method under test
        try {
            Whitebox.invokeMethod(videoActivity, "recordButtonListenerTemp");
        } catch (Exception e) {
            Assert.fail(UNIT_TEST_SETUP_ERROR + e.getMessage());
        }
    }
}
