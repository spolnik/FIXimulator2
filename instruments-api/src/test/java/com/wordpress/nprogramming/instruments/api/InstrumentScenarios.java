package com.wordpress.nprogramming.instruments.api;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class InstrumentScenarios {

    public static final String IBM_TICKER = "IBM";
    public static final String IBM_SEDOL = "2005973";
    public static final String IBM_NAME = "INTL BUSINESS MACHINES CORP";
    public static final String IBM_RIC = "IBM.N";
    public static final String IBM_CUSIP = "459200101";
    public static final String PRICE = "92.07";

    private Instrument instrument;

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        instrument = new Instrument(
                IBM_TICKER,
                IBM_SEDOL,
                IBM_NAME,
                IBM_RIC,
                IBM_CUSIP,
                PRICE
        );

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void canBeIdentifiedByTicker() throws Exception {
        assertThat(instrument.canBeIdentifiedBy(IBM_TICKER)).isTrue();
    }

    @Test
    public void canBeIdentifiedByName() throws Exception {
        assertThat(instrument.canBeIdentifiedBy(IBM_NAME)).isTrue();
    }

    @Test
    public void canBeIdentifiedBySedol() throws Exception {
        assertThat(instrument.canBeIdentifiedBy(IBM_SEDOL)).isTrue();
    }

    @Test
    public void canBeIdentifiedByCusip() throws Exception {
        assertThat(instrument.canBeIdentifiedBy(IBM_CUSIP)).isTrue();
    }

    @Test
    public void canBeIdentifiedByRic() throws Exception {
        assertThat(instrument.canBeIdentifiedBy(IBM_RIC)).isTrue();
    }

    @Test
    public void cannotBeIdentifiedByDummyId() throws Exception {
        assertThat(instrument.canBeIdentifiedBy("dummy")).isFalse();
    }

    @Test
    public void cannotHaveEmptyName() throws Exception {
        Instrument instrument = new Instrument(
                IBM_TICKER,
                IBM_SEDOL,
                "",
                IBM_RIC,
                IBM_CUSIP,
                PRICE
        );

        Set<ConstraintViolation<Instrument>> constraintViolations =
                validator.validate(instrument);

        hasViolationWithMessage(constraintViolations, "may not be empty");
    }

    @Test
    public void cannotHaveEmptyRic() throws Exception {
        Instrument instrument = new Instrument(
                IBM_TICKER,
                IBM_SEDOL,
                IBM_NAME,
                null,
                IBM_CUSIP,
                PRICE
        );

        Set<ConstraintViolation<Instrument>> constraintViolations =
                validator.validate(instrument);

        hasViolationWithMessage(constraintViolations, "may not be empty");
    }

    @Test
    public void cannotHaveEmptyTicker() throws Exception {
        Instrument instrument = new Instrument(
                "",
                IBM_SEDOL,
                IBM_NAME,
                IBM_RIC,
                IBM_CUSIP,
                PRICE
        );

        Set<ConstraintViolation<Instrument>> constraintViolations =
                validator.validate(instrument);

        hasViolationWithMessage(constraintViolations, "may not be empty");
    }

    @Test
    public void cannotHaveEmptyPrice() throws Exception {
        Instrument instrument = new Instrument(
                IBM_TICKER,
                IBM_SEDOL,
                IBM_NAME,
                IBM_RIC,
                IBM_CUSIP,
                ""
        );

        Set<ConstraintViolation<Instrument>> constraintViolations =
                validator.validate(instrument);

        hasViolationWithMessage(constraintViolations, "may not be empty");
    }

    @Test
    public void cannotHaveTooShortSedol() throws Exception {
        Instrument instrument = new Instrument(
                IBM_TICKER,
                IBM_SEDOL.substring(2),
                IBM_NAME,
                IBM_RIC,
                IBM_CUSIP,
                PRICE
        );

        Set<ConstraintViolation<Instrument>> constraintViolations =
                validator.validate(instrument);

        hasViolationWithMessage(constraintViolations, "length must be 7");
    }

    @Test
    public void cannotHaveTooShortCusip() throws Exception {
        Instrument instrument = new Instrument(
                IBM_TICKER,
                IBM_SEDOL,
                IBM_NAME,
                IBM_RIC,
                IBM_CUSIP.substring(2),
                PRICE
        );

        Set<ConstraintViolation<Instrument>> constraintViolations =
                validator.validate(instrument);

        hasViolationWithMessage(constraintViolations, "length must be 9");
    }

    private void hasViolationWithMessage(
            Set<ConstraintViolation<Instrument>> constraintViolations, String message
    ) {
        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.stream().findFirst().get().getMessage())
                .isEqualTo(message);
    }
}