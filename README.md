 
# Skelegram

A Skript Addon that allows users to create fully functional Telegram Bots.

# Documentation

[![SkriptHubViewTheDocs](http://skripthub.net/static/addon/ViewTheDocsButton.png)](http://skripthub.net/docs/?addon=Skelegram)

## Effects

### Telegram Bot Login
```
telegram login to bot %string% with token %string%
```

Create a session and authenticate to a telegram bot, given its username and its token.

<details>
	<summary>Telegram Bot Login</summary>
		
		```
            on load:
                telegram login to bot "SkriptItaly_bot" with token "SECRET"
        ```
</details>

### Clear all telegram sessions

```
(clear|stop) all telegram sessions
```

Close all the telegram sessions that are active. Stops any running bot.
You should use this in case you get an api error 409 saying multiple instances are running a single bot.
Please note that closing a session may take up to 50 seconds, as it is delayed by the Telegram API itself.

### Clear Specific Telegram Session

```
(clear|stop) telegram session of bot %string%
```

Closes the telegram session of a specific bot.
Please note that closing a session may take up to 50 seconds, as it is delayed by the Telegram API itself.

### Send telegram message

```
send telegram message %string/telegrammessage% [with markdown] to %telegramuser/telegramchat/number% [with bot %string%]
```

Send a telegram message. The message can be a string or can be an existing message.
The target chat ID can be used to specify the destination of the message. If used inside of a Telegram Event, the bot to use
is automatically detected. To use it outside of a Telegram Event, specify the username of the bot. If a session exists for
that bot, the message will be sent.

<details>
	<summary>Telegram Echo Bot</summary>

		```
            on telegram message:
                set {_mess} to event-telegram message #save received message in a variable
                send telegram message {_mess} to event-telegram user #echo their message
                send telegram message "*bold*" with markdown to event-telegram user #send message with markdown
        ```
</details>

### Delete telegram Message
```
delete telegram message %telegrammessage% [with bot %string%]
```

Delete a telegram message sent by the bot. If in a private chat, it can also delete messages from the other user.

<details>
	<summary>Telegram Delete Message</summary>

		```
            on telegram message:
                send telegram message "This message will autodelete!" to event-telegram user
                set {_last} to last sent telegram message
                wait 5 seconds
                delete telegram message {_last}
        ```
</details>

### Reply to telegram message

```
reply to telegram message %telegrammessage% with %telegrammessage/string% [with markdown] [with bot %-string%]
```

Send a telegram message, as a reply to another message. The reply message can be a string or can be an existing message.
If used inside of a Telegram Event, the bot to use is automatically detected.
To use it outside of a Telegram Event, specify the username of the bot. If a session exists for
that bot, the message will be sent.

<details>
	<summary>Telegram Reply to Message</summary>

		```
            on telegram message:
                set {_mess} to event-telegram message #save received message in a variable
                reply to telegram message {_mess} with "This message has been sent as a reply! Yay!" #
        ```
</details>

### Edit telegram message

```
edit telegram message %telegrammessage% to %telegrammessage/string% [with markdown] [with bot %string%]
```

Edit an existing, sent telegram message to a new one or to a string.

<details>
	<summary>Telegram Edit Message</summary>

		```
	on telegram message:
		reply to telegram message event-telegram message with "Welcome!"
		set {_mess} to last sent message
		wait 5 seconds
		edit telegram message {_mess} to "5 second have passed! Wow!"
        ```
</details>

### Copy telegram message

```
copy telegram message %telegrammessage% to %telegramuser/telegramchat/number% [with bot %string%]
```

Copy an existing telegram message, and send it to a user. This is useful for storing messages sent by the bot, eg. media
or other. A copied message will not have "forwarded from" to its top.

<details>
	<summary>Telegram Anonymous Report System</summary>

		```
	on telegram message:
		if text of event-telegram message contains "/report":
            reply to telegram message event-telegram message with "This message has been forwarded anonymously to the admins."
            copy telegram message event-telegram message to {@numeric_admin_id}
        ```
</details>

### Forward telegram message

```
forward telegram message %telegrammessage% to %telegramuser/telegramchat/number% [with bot %string%]
```

Forward an existing telegram message, and send it to a user. The final message will have information about who sent it in
the first place. It will show "forwarded from" to its top.

<details>
	<summary>Telegram Report System</summary>

		```
	on telegram message:
		if text of event-telegram message contains "/report":
            reply to telegram message event-telegram message with "This message has been forwarded to the admins."
            forward telegram message event-telegram message to {@numeric_admin_id}
        ```
</details>

### Answer to callback query

```
answer to callback query %callbackquery% with %string% [with popup]
```

Show a little notification to a user after they pressed a button. This will also complete a button press, removing the
little clock that shows on the button when it gets pressed. Adding "with popup" will show the callback query answer in
a popup window, and the user will have to press OK to hide it.

<details>
	<summary>Telegram Secret Code</summary>

		```
        on callback query with data "secret_code":
            delete telegram message message of event-callback query
            set {_code} to a random integer between 1000 and 9999
            answer to callback query event-callback query with "Your secret code is %nl%%{_code}%!" with popup
            
        ```
</details>

## Types

### Telegram Message
```
%telegrammessage%
event-telegram message
telegram message
```

This type holds all the information about a message.
You can either get this object in a telegram event (event-telegram message) or by creating a new one (new telegram message with text %string%)

<details>
	<summary>Telegram ID Retriever bot</summary>

		```
        on telegram message:
            send telegram message "%id of sender of event-telegram message%" to sender of event-telegram message
		```

</details>

### Telegram Chat
```
%telegramchat%
event-telegram chat
telegram chat
```

This type holds all the information about a telegram chat.

#### Usage
```
type of %telegramchat%
id of %telegramchat%
```

<details>
	<summary>Telegram Chat Type</summary>

		```
        on telegram message:
            send "Received a message from a chat type %type of chat of event-telegram message%." to console
		```

</details>

### Telegram User
```
%telegramuser%
event-telegram user
telegram user
```

This type represents a Telegram User.

#### Usage
```
id of %telegramuser%
```

<details>
	<summary>Send Telegram Message via ID</summary>

		```
        on load:
            send telegram message "Someone reloaded the skript!" to {@saved_user} with bot "{@username}"
		```

</details>


### Telegram Inline Keyboard
```
%inlinekeyboard%
inline keyboard
```

This type represents a Telegram Inline Keyboard. A keyboard is a matrix of buttons (double list). You can
access the rows of the keyboard with the row expression.

#### Usage
```
1st row of keyboard %inlinekeyboard%
inline keyboard of %telegrammessage%
```

<details>
	<summary>Remove Inline Keyboard of Message</summary>

		```
        on telegram message:
            if inline keyboard of event-telegram message is set:
                set {_mess} to event-telegram message
                delete inline keyboard of {_mess}
                edit telegram message event-telegram message to {_mess}
		```

</details>

### Telegram Inline Button
```
%inlinebutton%
inline button
```

This type represents a Telegram Inline Button. You can't add simply the button to a message, but you need first to add it
into a keyboard. You can then add the keyboard to the message. (Otherwise, how would Telegram know which position the button would
have to be?)

Of a button, you can define its text (the displayed button itself) and its action. If you set its URL, it will open automatically
a link when clicked. If you set its callback data, you can specify the data that is sent to the bot when clicked. You can
listen to such data with the EvtCallbackQuery

#### Usage
```
text of %inlinebutton%
callback data of %inlinebutton%
url of %inlinebutton%
```

<details>
	<summary>Send Message with Buttons</summary>

		```
        on telegram message:
            set {_mess} to a new telegram message with text "message content"
            set {_linkbutton1} to a new inline button with text "link button" with url "https://t.me/SkriptItaly"
            set {_linkbutton2} to a new inline button with text "link button 2" with url "https://t.me/SkriptItaly_bot"
            set {_databutton} to a new inline button with text "data button" with callback data "button_clicked"
            #the callback data will be listened in a on callback query event.
            set {_kb} to a new inline keyboard with buttons {_linkbutton1} and {_linkbutton2}
            set 2nd line of keyboard {_kb} to {_databutton}
            set inline keyboard of {_mess} to {_kb}
            send telegram message {_mess} to event-telegram user

        on callback query "button_clicked":
            send message "Someone clicked on the data button! We can do stuff down here!" to all players
		```

</details>

### Telegram Callback Query
```
%callbackquery%
callback query
```

This type represents a Callback Query. A callback query is what the bot receives when an inline button is pressed. You can see for example the callback data of the button with `callback data of %callbackquery%`

#### Usage
```
event-callback query
```

### Telegram Bot
```
%telegram bot%
telegram bot
```

This type represents a telegram bot. It holds information about the bot that fired a spefific event.
You can view its username (to check if the bot is the one you're creating in skript) with "%username of event-telegram bot%" or with "%event-telegram bot%"
Since Telegram Events are fired by all bots ran on a server, you should always filter that the received event is fired by
the bot you're programming.

#### Usage
```
event-telegram bot
```

## Events

### Telegram Message Event
```
on telegram message [event]
```

Fires whenever a message (or command) is received. You can access the received message with `event-telegram message`

<details>
	<summary>Send Telegram Messages in Minecraft Chat</summary>

        ```
		on telegram message:
            set {_mess} to event-telegram message
            send "&7[&9TG&7] &b%sender of {_mess}%&8: &7%text of {_mess}%" to all players
		```
</details>

### Telegram CallbackQuery Event
```
on [telegram] callback query [[with data] %string%]
```

Fires whenever a button with a callback data is fired. You can specify for which data to listen or leave it blank to
listen for any button press.

<details>
	<summary>Notify all players of a button press</summary>

        ```
		on callback query with data "button":
            send message "%event-telegram user% has clicked on a button!" to all players
		```
</details>


## Expressions

### New Telegram Message
```
new telegram message with text %string%
```

Returns a new empty telegram message. You can use other expressions to change its properties, such as text, keyboard, etc.
You may use this outside of a Telegram Event, but in order to send it you'll have to specify bot you are using.

<details>
	<summary>Send Custom Message</summary>

		```
		on telegram message:
            set {_mess} to a new telegram message with text "Hey!"
            send {_mess} to sender of event-telegram message
		```
</details>

### Last Telegram Sent Message
```
[the] last sent telegram message [with bot %string%]
```

Returns the last sent message. Use this exclusively and immediately after sending a message.

<details>
	<summary>Edit Sent Message</summary>

		```
		on telegram message:
            reply to telegram message event-telegram message with "Welcome!"
            set {_mess} to last sent message
            wait 5 seconds
            edit telegram message {_mess} to "5 second have passed! Wow!"
		```

</details>

### Telegram Chat of Message
```
chat of %telegrammessage%
```

Returns a Telegram Chat type. The chat in which a message has been sent. Can not be set.


### Telegram Text of Message
```
text of %telegrammessage%
```

Returns the text of a telegram message. Can be changed to edit the message.

<details>
	<summary>Telegram Console via Skript</summary>

	```
	on telegram message:
        if "%id of sender of event-telegram message%" is "{@admin_id}": #only execute commands from an admin id!
            make console execute text of event-telegram message
	```
</details>

### Telegram Id
```
id of %telegrammessage%
id of %telegramuser%
id of %telegramchat%
```

Returns the Numeric ID of a telegram object. Each user has an unique ID. The ID of a chat is identic to a user ID if it's
its private chat with the bot. (It is different if it's a group or supergroup etc.).
Each message has an unique ID inside of a chat. Can't be set.

### Chat Type
```
type of %telegramchat%
```

Returns the type of a telegram chat. It can be "PRIVATE", "BOT", "GROUP", "SUPERGROUP" or "CHANNEL". Can't be set.

### Callback Data of InlineButton
```
callback data of %inlinebutton%
```

The callback data of an Inline Button. This is the text that is sent to the bot on a button press, and it can be
listened for with the On Callback Query event.

### Callback Data of CallbackQuery
```
callback data of %callbackquery%
```

The callback data of a Callback Query. You can use this to check what telegram button was pressed, based on the callback data you had set.

### Text of InlineButton
```
text of %inlinebutton%
```

The Displayed text of an Inline Button. This is the actual message a button has in the chat.

### Url of InlineButton
```
url of %inlinebutton%
```

The URL of an Inline Button. Note that a button can't have both a callback data and an URL. The link will be opened
on button press.

### Row of InlineKeyboard
```
[the] %number%(st|nd|rd|th) row of [the] [inline] keyboard %inlinekeyboard%
```

The row of an existing InlineKeyboard. You can add, remove, set and clear rows. Setting a row that doesn't exist on the
keyboard will simply append it to the bottom.

### Telegram InlineKeyboard of Message
```
inline keyboard of %telegrammessage%
```

The Inline Keyboard of a Telegram Message. You can use this to add a keyboard to existing messages, before sending them.

### Telegram Sender of Message
```
sender of %telegrammessage%
```

The sender of a Telegram Message.

### Telegram Username of User / Chat / Bot
```
username of %telegramuser/telegramchat/telegrambot%
```

The username (mention) of a telegram user, a chat, or a bot. The username is returned without the @. If no username is
set, null is returned.

### Telegram Full Name of User / Chat
```
full name of %telegramuser/telegramchat%
```

The full name of a Telegram User or, if a Chat, the title of the chat.

### Telegram Language Code of User
```
language [code] of %telegramuser%
```

The language code (IT, EN, FR ... ) of a telegram user, as set in the settings by them.

### Telegram Replied Message
```
replied message of %telegrammessage%
```

Returns a Message object containing another message, the one that the original was in reply to. Cannot be set (to send messages as a reply, use the proper effect)

## FAQ

#### How to get a Bot Token?

You can create a new bot (and get a bot token) by talking with @BotFather on telegram. Simply start that bot, follow the
steps to create and customize the bot, and then copy the token from the long message you will get!


#### The server takes a long time to reload!

When the Addon is disabled (on a reload or a restart), all of the active telegram sessions are stopped. Because of how telegram works, stopping a session can take up to 50 seconds. The best practice for this is to stop the server (instead of reloading it), but you can't do much about it.


#### Stopping a session takes a long time!

Stopping a session takes up to 50 seconds. When you execute the effect "clear all telegram sessions" it will try to stop all of them and wait for a confirmation from the API, which is delayed by about 50 seconds.

#### The console is getting spammed from an API error 409!

As the error says, it's because you are running multiple instances of the bot. Make sure that you are not using the same token on different skripts that control the bot. If the error persists, run the effect "clear all telegram sessions" and wait for it to be finished. (To know when its finished add a broadcast right after the effect)



