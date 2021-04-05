package org.springframework.samples.petclinic.sample;

public class SampleController {

	SampleRepository sampleReository;

	public SampleController(SampleRepository sampleReository) {
		this.sampleReository = sampleReository;
	}

	public void doSomthing() {
		sampleReository.save();
	}
}
