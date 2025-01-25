package com.pranavsalaria.File_Upload_Download_Local_Server.service;

import com.pranavsalaria.File_Upload_Download_Local_Server.dto.ValidateRequest;

public interface ValidateTokenService {
    public String validateToken(ValidateRequest validateRequest);

    public String getUserName(ValidateRequest validateRequest);
}
