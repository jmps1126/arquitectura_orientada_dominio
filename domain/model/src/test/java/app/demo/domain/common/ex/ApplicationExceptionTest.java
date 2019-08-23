package app.demo.domain.common.ex;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class ApplicationExceptionTest {

    @Test
    public void testExceptionStructure() {
        ApplicationException exception = new ApplicationException("msj", "code001");
        assertThat(exception).hasMessage("msj");
        assertThat(exception.getCode()).isEqualTo("code001");
    }
}