package com.project.authappfrontend;

import com.project.authappfrontend.models.AuthRequest;
import com.project.authappfrontend.models.ChangePasswordRequest;
import com.project.authappfrontend.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @POST("/authenticate")
    Call<ResponseBody> authenticateUser(@Body AuthRequest authRequest);

    @PATCH("/api/password/reset")
    Call<ResponseBody> resetPassword(@Query("name") String username, @Query("email") String email);

    @PATCH("/api/password/change")
    Call<ResponseBody> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @PATCH("/api/profile/update")
    Call<ResponseBody> updateProfile(@Body User user);

    @POST("/api/users/onboard")
    Call<ResponseBody> onboardUser(@Body User user);

    //handle logout API FE and BE
    /*@POST("/users/logout")
    Call<ResponseBody> logoutUser(@Body String username);*/
}
