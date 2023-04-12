# MY-ISM

<p>This is an Android app designed for MyISM students to get the keys for their SAC room.</p>

<h2>Problem Statement</h2>
<p>I frequently go to SAC, and there are a lot of times when I don’t get the key since the guard there will tell me to come after 5 pm, or it will take a lot of time for the guard to give me the key and I have to enter my details manually in the record book. Can’t we automate this by making some kind of locker system? Let me elaborate.</p>
<p>If you’ve ever used the ATM, you put in your card and your pin, enter your withdrawal amount, get your cash, and that’s it. Take a similar idea where you can authenticate using the ID card photo of your club coordinator, do some kind of credential authentication, or maybe have an application by which you will scan the QR code, and that will call an API to make the motors unlock your locker with the help of your microcontroller.</p>
<p>You could also think of something more innovative or another feature to add to this system.</p>
<h2>Approach</h2>
<p>The proposed solution to this problem is to develop a locker system that will allow authorized users to access the key to their SAC room. The system will use ID card photo authentication, credential authentication, or QR code scanning to verify the user's identity. Once the user is authenticated, the system will use an API to communicate with a microcontroller to unlock the locker containing the key to the user's room. </p>
<p>One possible feature that can be added to the system is the ability to reserve a time slot for key pickup. This feature will allow users to reserve a specific time slot for key pickup, eliminating the need to wait in line or come back later in the day.</p>

<h2>Features</h2>
<ul>
	<li>User registration</li>
	<li>Display necessary details to get the key</li>
	<li>Record check-in and check-out time</li>
	<li>Submit key back</li>
	<li>Record time of key submission by security guard</li>
</ul>

<h2>Details Stored</h2>
<ul>
	<li>Name</li>
	<li>Admission number</li>
	<li>Room number</li>
	<li>Time</li>
	<li>Check-in time</li>
	<li>In time</li>
</ul>

<h2>Usage</h2>
<ol>
	<li>User registers using their details</li>
	<li>Upon successful registration, user can view their details to get the key</li>
	<li>When user submits the key back, they can select the option to record the current time</li>
	<li>The security guard can submit the key to the keybox and enter the key details in the mobile app to record the time of key submission</li>
</ol>

<h2>Technologies Used</h2>
<ul>
	<li>Android Studio</li>
	<li>Java programming language</li>
	<li>Firebase database</li>
</ul>

<h2>Installation</h2>
<ol>
	<li>Clone the repository from Github.</li>
	<li>Open Android Studio and import the project.</li>
	<li>Connect to Firebase database.</li>
	<li>Run the app on an Android device or emulator.</li>
</ol><table>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/79053599/230706813-ca790590-1580-4760-8280-cdfd9188a284.jpg" width="200" height="400"></td>
    <td><img src="https://user-images.githubusercontent.com/79053599/230713251-196e7fb7-1471-4db4-853b-e6365add3d2f.png" width="200" height="400"></td>



  </tr>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/79053599/230757948-1bb4d8b2-a743-4908-a606-2a6f36e38728.png" width="200" height="400"></td>
    <td><img src="https://user-images.githubusercontent.com/79053599/230757963-20204c64-12d7-4b58-81f1-cc7dc22f5095.png" width="200" height="400"></td>
  


  </tr>
</table>

<h1>MY-ISM Automatic Key Dispenser System</h1>
	<p>This repository contains the code for an automatic key dispenser system for the MY-ISM Android app. The system uses a Raspberry Pi to dispense the keys for the SAC room. The integration of the dispenser system with the MY-ISM app allows the users to request the key for a particular SAC room from their mobile device.</p>

<h2>Features</h2>
<ul>
	<li>Request for a specific SAC room key from the MY-ISM app.</li>
	<li>The app generates a random password for authentication purposes.</li>
	<li>Enter the admission number and the random password on the Raspberry Pi connected to the servo motor.</li>
	<li>The servo motor will release the key once it gets rotated.</li>
</ul>

<h2>Technologies Used</h2>
<ul>
	<li>Raspberry Pi</li>
	<li>Python programming language</li>
	<li>Firebase database</li>
	<li>MY-ISM Android app</li>
</ul>


<h2>Conclusion</h2>
<p>The MY-ISM automatic key dispenser system provides a convenient and efficient way for users to access the keys to their SAC room. The integration of the dispenser system with the MY-ISM app makes the process of key pickup seamless and hassle-free. The use of modern technologies like Raspberry Pi and Firebase database makes the system reliable and secure.</p>


