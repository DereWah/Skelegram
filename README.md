
# Skelegram

A Skript Addon that allows users to create fully functional Telegram Bots.

[Documentation](https://skripthub.net/docs/?addon=Skelegram)


## Documentation

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
clear all telegram sessions
```

Close all the telegram sessions that are active. Stops any running bot.
You should use this in case you get an api error 409 saying multiple instances are running a single bot.

### Send telegram message

```
send telegram message %string/telegrammessage% to %telegramuser/telegramchat/number% [with bot %string%]
```

Send a telegram message. The message can be a string or can be an existing message.
The target chat ID can be used to specify the destination of the message. If used inside of a Telegram Event, the bot to use
is automatically detected. To use it outside of a Telegram Event, specify the username of the bot. If a session exists for
that bot, the message will be sent.

<details>
	<summary>Telegram Echo Bot</summary>

		```
            on telegram message:
                set {_mess} to event-telegrammessage #save received message in a variable
                send telegram message {_mess} to event-telegramuser #echo their message
        ```
</details>


## Types

### Telegram Message
```
%telegrammessage%
%telegrammessages%
telegram message
```

This type holds all the information about a received message. As of right now you can only get this type from an
on telegram message event, meaning you can't really build a new message from scratch.

#### Usage
```
set {_mess} to event-telegram message # in an on telegram message event
set text of {_mess} to "heya"
set {_user} to sender of {_mess}
```

<details>
	<summary>Telegram ID Retriever bot</summary>

		```
        on telegram message:
            send "%id of sender of event-telegram message%" to sender of event-telegram message
		```

</details>

### Telegram Chat

### Telegram User

### Telegram InlineKeyboard

### Telegram InlineButton

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

## Expressions

### Event Telegram Message
```
[event-]telegram message
```

Returns a telegram message type from inside of a Telegram Message event.

<details>
	<summary>Telegram Message ID</summary>

		```
		on telegram message:
            set {_mess} to event-telegram message
            set {_id} to id of {_mess}
            set {_chatid} to id of chat of event-telegram message
            send telegram message "This message has ID %{_id}% in chat %{_chatid}%!" to sender of {_mess}
		```
</details>

### New Telegram Message
```
new telegram message
```

Returns a new empty telegram message. You can use other expressions to change its properties, such as text, keyboard, etc.
You may use this outside of a Telegram Event, but in order to send it you'll have to specify bot you are using.

<details>
	<summary>Send Custom Message</summary>
		```
		on telegram message:
            set {_mess} to a new telegram message
            set text of {_mess} to "Hey!"
            send {_mess} to sender of event-telegram message
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

Return the Numeric ID of a telegram object. Each user has an unique ID. The ID of a chat is identic to a user ID if it's
its private chat with the bot. (It is different if it's a group or supergroup etc.).
Each message has an unique ID inside of a chat.

### Telegram Chat Type

### Telegram Callback Data of InlineButton

### Telegram Text of InlineButton

### Telegram Url of InlineButton

### Telegram Line of InlineKeyboard

### Telegram InlineKeyboard of Message

### Telegram Sender of Message

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



