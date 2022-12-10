# FamilEat - Android app for sharing and hosting dinners. 

### Don't you have a place to have a Shabbat dinner?
### Are you hosting a meal and have space available to host people?
### FamilEat is here for you!
### With FamilEat no one will eat alone.


<img src="https://user-images.githubusercontent.com/86108478/206856224-ab026cac-bcda-4bcc-9b0f-c44eeaa85034.jpg" alt="drawing" width="800"/>


# General information: 
In this application you can register as a host user or guest user, the only place where you can choose the type of user is on the registration screen. <br>
After registration, you will be required to verify your account using a link that will be sent to the email account you specified during registration, with this email address you will connect to the application afterwards.<br>
All information about the users and the meals is stored in Google's realtime database Firebase which serves as the main server of the application.<br>
The minimum age to use the application is 16.

### Types of users:
#### Host:
a user of the host type has the option of creating a new meal, viewing and editing the meals he created, viewing requests from users to join meals, the option to decide whether to approve or delete the request and contacting his meal guests.
#### Guest:
A guest type user has the option to view all the meals that the hosts have posted, choose which meal he wants to join and send a request to join.
A guest will be able to view his meals (the meals he sent a request and the host approved)
and contact the hosts of his meals.

# Application screens: 
## General screens:
### Login screen- The start activity:
On this screen you can connect to the system using an email address and password, if you are not a registered user you can go to the registration screen, if you do not remember the old password go to the password recovery screen, if you have not verified the email account you will not be able to log in until you do so, if you are a host type user, after Registration will go to the hosts home screen, if you are a guest type user, after registration you will go to the hosts home screen.
There is an option to mark "remember me" and on subsequent connections the application will go to your home screen automatically until you click logout.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/206835169-e255f592-4f82-42af-acfc-b956b7841744.jpg" alt="drawing" width="200"/>

### Register screen:
On this screen you can register for the system using an email address and password, by clicking the "sign up" button the system will check the legality of the inputs and your age (over 16), if everything is correct your details will be registered in the system and your user will become active after you verify your email.
This screen is the only place where you can choose your user type (host or guest).<br>
<br><img src="https://user-images.githubusercontent.com/86108478/206836499-11497024-933d-44c5-9705-02f5dcac6607.jpg" alt="drawing" width="200"/>

### Forgot Password screen:
On this screen you can reset your password in case you forgot it, all you have to do is enter your email address click on "reset password" and you will be sent an email with a link, when you click on the link you can enter a new password and connect to the system with it.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/206837807-b0ac41ea-ffb1-4d18-9fa1-f516a8790491.jpg" alt="drawing" width="200"/>

### Edit profile screen:
This screen can be entered from both a host and a guest user, you can edit your full name, date of birth and gender on this screen.<br>
(Password can be edited using the "Forgot password" button, email and user type cannot be edited).<br>
<br><img src="https://user-images.githubusercontent.com/86108478/206850090-2b56cba8-0c6b-4380-9fad-9ac3dd4ed6b7.jpg" alt="drawing" width="200"/>

## Hosts screens:
### Host main screen:
This screen is the screen that host type users go to after logging in, this is the home screen for hosts.<br>
In this screen there are options of adding a new meal, editing a profile and logging out.<br>
The meals created by the host are displayed on the screen, clicking on a meal opens up more details about the meal and additional options for the meal.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/206849748-d87b3170-e3ae-476e-bca4-ef63c16659fe.jpg" alt="drawing" width="200"/>

### Dinner options:
By clicking on a meal, the details of the meal open up and options for editing and deleting appear..<br>
The meals created by the host are displayed on the screen, clicking on a meal opens up more details about the meal and additional options for the meal.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/206855101-79c07fef-b7c1-4f33-a73c-c88ec2778ca2.jpg" alt="drawing" width="200"/>

### Submit dinner screen
On this screen, a host can enter a new meal into the system by entering the details of the meal, which are title, date, time, number of guests, kosher, photo and free text.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/206855392-708767f9-dde5-4bd2-8352-0b03e97ae98e.jpg" width="200"/>
## Guests screens:
