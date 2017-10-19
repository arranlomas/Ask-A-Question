# Ask-A-Question
App to send a question

1) Join or create a channel using the nav bar

2) Press the add button, type your question and press send

3) ou and other users of the app can vote on the questions (one vote per question per device)

Apk's can be downloaded from the following links:

To use the app you have to generate the apk
clone the repo
go to firebase create an android app in firebase, enable realtime database, enable auth, enable annonymous auth, download the json configuration put in the app under /app/ directory

To install you must go to:
settings -> security -> check Unknown sources (also recommend you check Verify apps) -> open the apk and follow hints to install

The app is secured by you using your own firebase configuration, this means no other instances of the app can access it, channels can be created and removed, if someone you dont want to see your questions still has the app, simply create a new channel with a new password and they cannot see the questions in this new channel


Screenshots:

![Main Screen](https://github.com/arranlomas/Ask-A-Question/blob/master/screenshots/ask-a-question-screenshot-b.png)

![Add question dialog](https://github.com/arranlomas/Ask-A-Question/blob/master/screenshots/ask-a-question-screenshot-a.png)

![Channel Drawer](https://github.com/arranlomas/Ask-A-Question/blob/master/screenshots/ask-a-question-screenshot-c.jpg)

![Add channel dialog](https://github.com/arranlomas/Ask-A-Question/blob/master/screenshots/ask-a-question-screenshot-d.jpg)

![Join channel dialog](https://github.com/arranlomas/Ask-A-Question/blob/master/screenshots/ask-a-question-screenshot-e.jpg)
