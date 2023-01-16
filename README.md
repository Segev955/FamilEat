# FamilEat - Android app for sharing and hosting dinners. 

### Don't you have a place to have a Shabbat dinner?
### Are you hosting a meal and have space available to host people?
### FamilEat is here for you!
### With FamilEat no one will eat alone.


<img src="https://user-images.githubusercontent.com/86108478/212748228-d3f94d11-2fd0-45b2-a756-c9f6157dacd0.png" alt="drawing" hight= "200" width="800"/>


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
On this screen you can connect to the system using an email address and password, if you are not a registered user you can go to the registration screen, if you do not remember your password go to the password recovery screen, if you have not verified the email account you will not be able to log in until you do so, if you are a host type user, after Registration will go to the hosts home screen, if you are a guest type user, after registration you will go to the guests home screen.
There is an option to mark "remember me" and on subsequent connections the application will go to your home screen automatically until you click logout.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212748446-2572eabf-a83a-466e-9644-1d3fbf189ed8.jpg" alt="drawing" width="200"/>

### Register screen:

On this screen you can register for the system using an email address and password, by clicking the "sign up" button the system will check the legality of the inputs and your age (over 16), if everything is correct your details will be registered in the system and your user will become active after you verify your email.
This screen is the only place where you can choose your user type (host or guest).<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212748744-1b866ead-4509-4f82-821b-7ad8a9bb7e6e.jpg" alt="drawing" width="200"/>

### Forgot Password screen:
On this screen you can reset your password in case you forgot it, all you have to do is enter your email address click on "reset password" and you will be sent an email with a link, when you click on the link you can enter a new password and connect to the system with it.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212749311-fa28d33a-b8c4-4c51-9505-44934afad0d4.jpg" alt="drawing" width="200"/>

### Edit profile screen:
This screen can be entered from both a host and a guest user, you can edit your full name, date of birth and gender on this screen.<br>
(Password can be edited using the "Forgot password" button, email and user type cannot be edited).<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212749526-7a7f0d82-3a2b-4f1f-b321-e43119e74f9a.jpg" alt="drawing" width="200"/>

## Hosts screens:
### Host main screen:
This screen is the screen that host type users go to after logging in, this is the home screen for hosts.<br>
In this screen there are options of adding a new meal, editing a profile and logging out.<br>
The meals created by the host are displayed on the screen, there is a filter option in the meal display according to kosher and date. <br>The host has the option from this page to view his past meals.<br>clicking on a meal opens up more details about the meal and additional options for the meal.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212753073-73843907-fee2-4aa6-a473-8cb3be767cfe.jpg" alt="drawing" width="200"/>

### Submit dinner screen
On this screen, a host can enter a new meal into the system by entering the details of the meal, which are title, date, time, number of guests, kosher, photo and free text.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212751229-b2f8a125-d251-4def-9178-58cbe26833e5.jpg" width="200"/>

### Dinner filter:
In this example, the use of filters is shown, in the kosher filter we chose "Kosher Dairy" and in "Starting From" we selected a date and after clicking "SEARCH" only dairy meals dated 1/28/23 and later will appear.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212752942-4b344057-3a02-4c49-920d-6f90914ab65f.jpg" alt="drawing" width="200"/>

### Dinner options:
By clicking on a meal, the details of the meal open up and options for chat, editing and deleting appear.<br>
Clicking on delete meal will delete the meal and all its data.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212750949-508e0e18-c5b0-4a88-ba42-09a8d325ba22.jpg" alt="drawing" width="200"/>

### Edit Dinner:
By clicking on a "EDIT DINNER", a form for editing the meal will open with the meal details entered in advance, the details can be edited and if everything is correct in the inputs, the meal data will change.<br>
Clicking on delete meal will delete the meal and all its data.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212754640-5a6cb0ff-c783-4e5d-8d78-ffaf0496feef.jpg" alt="drawing" width="200"/>


### Dinner Chat:
By clicking on "CHAT", a group chat screen opens for all participants of the meal.<br>
You can write messages that will appear to all participants in the meal, it also shows the date and time the message was sent, who sent the message and if he is the Host of the meal or or being a Guest.<br>
Only the host has an option to clear the chat.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212755837-48d5af62-d66f-41dd-8de8-60743e815a91.jpg" alt="drawing" width="200"/>

### Requests:
When the Host receives a request to join to a meal, the bell above will be marked in green, and above it will appear the number of requests the Host has.<br>
By pressing the bell, a window will open in which the content of the request is displayed, for which meal and who the requester is, the Host can decide whether to approve the request or refuse it by clicking "ACCEPT" or "REJECT".<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212758478-a5921271-b094-4a48-9dfe-e986c66c3b71.jpg" alt="drawing" width="200"/>



## Guests screens:
### Guest main screen:
This screen is the screen that guest type users go to after logging in, this is the home screen for guests.<br>
In this screen there are options editing a profile and logging out.<br>
The meals shown on the screen are the available meals whose time has not yet passed, you can click on "SEND REQUEST" to send the host a request to join the meal, and "CANCEL REQUEST" to cancel the sent request.<br>By clicking on "MY MEALS", the meals that will be displayed are the meals that the guest has been approved for by the host, there is a filter option in the meal display according to kosher and date. <br>The guest has the option from this page to view his past meals.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212757533-beff8865-0e60-4d2a-97e3-7984c52d141e.jpg" alt="drawing" width="200"/>

### Dinner filter:
In this example, the use of filters is shown, in the kosher filter we chose "Not Kosher" and in "Starting From" we selected a date and after clicking "SEARCH" only dairy meals dated 6/3/23 and later will appear.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212759343-2fd47eda-cfae-474e-a126-a39e0e400816.jpg" alt="drawing" width="200"/>

### Guest "My Meals":
By clicking on "My meals" the meals that the guest has been approved for by the host and whose time has not yet passed will appear on the screen, for these meals the guest will be invited.<br>
In this mode, in each meal there is the option to enter the group chat, and by clicking "exit the meal" you can leave the meal and cancel your participation in it.
<br><img src="https://user-images.githubusercontent.com/86108478/212760335-5aafaced-8259-48eb-beb6-c71d4d526561.jpg" alt="drawing" width="200"/>

### Dinner Chat:
By clicking on "CHAT", a group chat screen opens for all participants of the meal.<br>
You can write messages that will appear to all participants in the meal, it also shows the date and time the message was sent, who sent the message and if he is the Host of the meal or or being a Guest.<br>
Guests has no option to clear the chat.<br>
<br><img src="https://user-images.githubusercontent.com/86108478/212761065-263d6e0d-d929-4399-a3fb-692489cfe266.jpg" alt="drawing" width="200"/>
