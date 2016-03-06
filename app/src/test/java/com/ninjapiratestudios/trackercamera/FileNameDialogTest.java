package com.ninjapiratestudios.trackercamera;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import junit.framework.Assert;

import org.junit.runner.RunWith;

import static org.mockito.Matchers.*;

import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.regex.Matcher;

/**
 * Unit tests for FileNameDialog.java.
 *
 * @author John Qualls
 * @version 3/5/2016
 */
@PrepareForTest(FileNameDialog.class)
@RunWith(PowerMockRunner.class)
public class FileNameDialogTest extends BaseTest {
    private FileNameDialog dialog;

    @Before
    public void setup() {
        try {
            dialog = Mockito.spy(new FileNameDialog());
        } catch (Exception e) {
            Assert.fail(UNIT_TEST_BEFORE_ERROR + e.getMessage());
        }
    }

    @Test
    public void correctStyleTest() {
        // Test setup
        try {
            PowerMockito.suppress(PowerMockito.methods(DialogFragment.class,
                    "onCreate"));
            Mockito.doNothing().when(dialog).setStyle(anyInt(),
                    anyInt());
        } catch (Exception e) {
            Assert.fail(UNIT_TEST_SETUP_ERROR + e.getMessage());
        }

        // Execute SUT
        try {
            dialog.onCreate(null);
        } catch (Exception e) {
            Assert.fail(UNIT_TEST_SUT_ERROR + e.getMessage());
        }

        // Run test
        try {
            Mockito.verify(dialog).setStyle(DialogFragment.STYLE_NO_TITLE,
                    android.R.style.Theme_Holo_Dialog);
        } catch (Exception e) {
            Assert.fail(UNIT_TEST_EXECUTE_ERROR + e.getMessage());
        }
    }

    @Test
    public void correctButtonListenersTest() {
        LayoutInflater inflater = Mockito.mock(LayoutInflater.class);
        ViewGroup container = Mockito.mock(ViewGroup.class);
        View view = Mockito.mock(View.class);

        // Test setup
        try {
            Mockito.when(inflater.inflate(anyInt(), any(ViewGroup.class),
                    anyBoolean())).thenReturn(view);
            Mockito.when(view.findViewById(anyInt())).thenReturn(view);
            PowerMockito.whenNew(FileNameDialog.ButtonClick.class)
                    .withNoArguments().thenReturn(null);
        } catch (Exception e) {
            Assert.fail(UNIT_TEST_SETUP_ERROR + e.getMessage());
        }

        // Execute SUT
        try {
            dialog.onCreateView(inflater, container,
                    Mockito.mock(Bundle.class));
        } catch (Exception e) {
            Assert.fail(UNIT_TEST_SUT_ERROR + e.getMessage());
        }

        // Run test
        try {
            Mockito.verify(inflater).inflate(R.layout.fragment_file_name_dialog,
                    container, false);
        } catch (Exception e) {
            Assert.fail(UNIT_TEST_EXECUTE_ERROR + e.getMessage());
        }
    }
}
