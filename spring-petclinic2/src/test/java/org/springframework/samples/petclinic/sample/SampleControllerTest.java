package org.springframework.samples.petclinic.sample;

import org.junit.jupiter.api.Test;

class SampleControllerTest {

    @Test
    void testDoSomthing() {
    	// DI 설명을 위한 코드
		SampleRepository sampleRepository = new SampleRepository();
		SampleController sampleController = new SampleController(sampleRepository);
		sampleController.doSomthing();
    }
}
