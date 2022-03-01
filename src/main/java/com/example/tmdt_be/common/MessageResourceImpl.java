package com.example.tmdt_be.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageResourceImpl implements MessageResource {

    @Autowired
    MessageSource messageSource;
    Locale localeVi = new Locale("vi");

    @Nullable
    public String getMessage(String var1, @Nullable Object[] var2, @Nullable String var3) {
        return messageSource.getMessage(var1, var2, var3, localeVi);
    }

    public String getMessage(String var1) throws NoSuchMessageException {
        return messageSource.getMessage(var1, null, localeVi);
    }

    public String getMessage(String var1, @Nullable Object[] var2) throws NoSuchMessageException {
        return messageSource.getMessage(var1, var2, localeVi);
    }

    public String getMessage(MessageSourceResolvable var1) throws NoSuchMessageException {
        return messageSource.getMessage(var1, localeVi);
    }
}
