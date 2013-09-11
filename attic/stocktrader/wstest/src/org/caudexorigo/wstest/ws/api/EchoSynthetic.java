package org.caudexorigo.wstest.ws.api;

import org.caudexorigo.wstest.domain.Synthetic;

public class EchoSynthetic
{
	protected Synthetic synth;

	public Synthetic getSynth()
	{
		return this.synth;
	}

	public void setSynth(Synthetic synth)
	{
		this.synth = synth;
	}
}