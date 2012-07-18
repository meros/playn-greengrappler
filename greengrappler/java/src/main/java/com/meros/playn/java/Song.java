package com.meros.playn.java;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.meros.playn.core.Music.AbstractSong;
import com.meros.playn.core.micromod.IBXM;
import com.meros.playn.core.micromod.Module;

public class Song implements AbstractSong
{
	private static final int SAMPLE_RATE = 48000;

	private Module module;
	private IBXM ibxm;
	private SourceDataLine audioLine;
	
	private enum State
	{
		STOPPED,
		PLAYING
	}
	
	private State myState = State.STOPPED;

	public Song(InputStream aIs) throws LineUnavailableException, IOException
	{
		loadModule(aIs);

		AudioFormat audioFormat = new AudioFormat( SAMPLE_RATE, 16, 2, true, true );
		audioLine = AudioSystem.getSourceDataLine( audioFormat );
		audioLine.open();
		audioLine.start();
	}

	private synchronized void loadModule( InputStream aIs ) throws IOException {		
		byte[] moduleData = new byte[ ( int ) aIs.available() ];
		int offset = 0;
		while( offset < moduleData.length ) {
			int len = aIs.read( moduleData, offset, moduleData.length - offset );
			if( len < 0 ) throw new IOException( "Unexpected end of file." );
			offset += len;
		}
		aIs.close();
		module = new Module( moduleData );
		ibxm = new IBXM( module, SAMPLE_RATE );
	}

	byte[] outBuffer;
	int outIdx = 0;
	int outOffs = 0;

	public void update() {
		
		if (myState != State.PLAYING)
			return;

		if (outOffs >= outIdx)
		{
			int[] buffer = new int[ibxm.getMixBufferLength()];
			outBuffer = new byte[buffer.length*4];
			outOffs = 0;
			outIdx = 0;
			
			int size = ibxm.getAudio(buffer);

			for( int mixIdx = 0, mixEnd = size * 2; mixIdx < mixEnd; mixIdx++ ) {
				int ampl = buffer[ mixIdx ];
				if( ampl > 32767 ) ampl = 32767;
				if( ampl < -32768 ) ampl = -32768;
				outBuffer[ outIdx++ ] = ( byte ) ( ampl >> 8 );
				outBuffer[ outIdx++ ] = ( byte ) ampl;
			}
		}
		
		int writeLen = Math.min(outIdx-outOffs, audioLine.available());
		outOffs += audioLine.write(outBuffer, outOffs, writeLen);
	}

	public void play() {
		myState = State.PLAYING;
		audioLine.start();
	}

	public void stop() {
		myState = State.STOPPED;
		audioLine.stop();
	}
}
