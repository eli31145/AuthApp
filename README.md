"# AuthApp" 
User Journey:
When app launches, user can either 
1) log in with existing account (calling the /authenticate endpoint, Authenticate API) before proceeding to dashboard page OR
2) Sign up (calling the api/user/onboard endpoint, Onboard API)

In the dashboard page, depending on the user's Role (USER/ADMIN), they can see 2 (Change Password, Update Profile) or 3 buttons (Change Password, Update Profile, Reset Password) respectively. Clicking on the chosen button will lead to opening of each respective page with their own flows. 

User with Role of 'USER' can either
1) Change Password (calling the /api/password/change endpoint, Change Password API) OR
2) Update Profile (calling the /api/profile/update endpoint, Update Profile API)
while 'ADMIN's can have an additional function of 
3) Reset Password (calling the /api/password/reset endpoint, Reset Password API)
