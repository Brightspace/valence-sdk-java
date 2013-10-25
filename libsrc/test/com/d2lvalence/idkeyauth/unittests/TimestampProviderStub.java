package com.d2lvalence.idkeyauth.unittests;

import com.d2lvalence.idkeyauth.implementation.ITimestampProvider;

public class TimestampProviderStub implements ITimestampProvider {
    
    @Override
    public long getCurrentTimestampInMilliseconds() {
            return m_milliseconds;
    }

    public TimestampProviderStub( long milliseconds ) {
            m_milliseconds = milliseconds;
    }

    private final long m_milliseconds;

}
