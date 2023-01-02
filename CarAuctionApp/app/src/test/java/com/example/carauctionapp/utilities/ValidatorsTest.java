package com.example.carauctionapp.utilities;

import org.junit.Test;

import static org.junit.Assert.*;


public class ValidatorsTest {

    @Test
    public void validateTextFieldInputData_ReturnsFalse_WhenDataContainsIllegalCharacters() {
        final String inputData = "<SampleData'./n";

        assertFalse(Validators.validateTextFieldInputData(inputData, true));
    }

    @Test
    public void validateTextFieldInputData_ReturnsFalse_WhenDataContainsDigits_And_DigitsAreNotAllowed() {
        final String inputData = "SampleData123";

        assertFalse(Validators.validateTextFieldInputData(inputData, false));
    }

    @Test
    public void validateTextFieldInputData_ReturnsTrue_WhenDataContainsDigits_And_DigitsAreAllowed() {
        final String inputData = "SampleData123";

        assertTrue(Validators.validateTextFieldInputData(inputData, true));
    }

    @Test
    public void validateTextFieldInputData_ReturnsTrue_WhenDataDoesNotContainDigitsOrIllegalCharacters_And_DigitsAreAllowed() {
        final String inputData = "SampleData";

        assertTrue(Validators.validateTextFieldInputData(inputData, true));
    }

    @Test
    public void validatePasswordFieldInputData_ReturnsFalse_WhenPasswordDoesNotContain_LowerCaseCharacters() {
        final String inputData = "PASSWORD123";

        assertFalse(Validators.validatePasswordFieldInputData(inputData));
    }

    @Test
    public void validatePasswordFieldInputData_ReturnsFalse_WhenPasswordDoesNotContain_UpperCaseCharacters() {
        final String inputData = "password123";

        assertFalse(Validators.validatePasswordFieldInputData(inputData));
    }

    @Test
    public void validatePasswordFieldInputData_ReturnsFalse_WhenPasswordDoesNotContain_Digits() {
        final String inputLowerData = "password";
        final String inputHigherData = "PASSWORD";

        assertFalse(Validators.validatePasswordFieldInputData(inputLowerData));
        assertFalse(Validators.validatePasswordFieldInputData(inputHigherData));
    }

    @Test
    public void validatePasswordFieldInputData_ReturnsFalse_WhenNoLegalCharactersProvided() {
        final String inputData = "Password123";

        assertFalse(Validators.validatePasswordFieldInputData(inputData));
    }

    @Test
    public void validatePasswordFieldInputData_ReturnsTrue_WhenValidPasswordProvided() {
        final String passwordData1 = "Password123@";
        final String passwordData2 = "+Password123";
        final String passwordData3 = "-Password123_";

        assertTrue(Validators.validatePasswordFieldInputData(passwordData1));
        assertTrue(Validators.validatePasswordFieldInputData(passwordData2));
        assertTrue(Validators.validatePasswordFieldInputData(passwordData3));
    }

    @Test
    public void validatePhoneFieldInputData_ReturnsFalse_WhenPhone_Contains_UpperCaseCharacters() {
        final String phoneData1 = "P+3100000";
        final String phoneData2 = "P3100000";

        assertFalse(Validators.validatePhoneFieldInputData(phoneData1));
        assertFalse(Validators.validatePhoneFieldInputData(phoneData2));
    }

    @Test
    public void validatePhoneFieldInputData_ReturnsFalse_WhenPhone_Contains_LowerCaseCharacters() {
        final String phoneData1 = "p+3100000";
        final String phoneData2 = "p3100000";

        assertFalse(Validators.validatePhoneFieldInputData(phoneData1));
        assertFalse(Validators.validatePhoneFieldInputData(phoneData2));
    }

    @Test
    public void validatePhoneFieldInputData_ReturnsFalse_WhenPhone_Contains_IllegalCharacters() {
        final String phoneData1 = "+310000<>_-*@'/";
        final String phoneData2 = "310000<>_-*@'/";

        assertFalse(Validators.validatePhoneFieldInputData(phoneData1));
        assertFalse(Validators.validatePhoneFieldInputData(phoneData2));
    }

    @Test
    public void validatePhoneFieldInputData_ReturnsTrue_WhenValidPhoneProvided() {
        final String phoneData1 = "+3725821342";
        final String phoneData2 = "+4032153214";

        assertTrue(Validators.validatePhoneFieldInputData(phoneData1));
        assertTrue(Validators.validatePhoneFieldInputData(phoneData2));
    }
}