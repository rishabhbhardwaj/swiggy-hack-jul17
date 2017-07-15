// ==UserScript==
// @name        UserNotify
// @include     https://www.swiggy.com/checkout
// @require     http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js
// @require     https://cdn.pubnub.com/sdk/javascript/pubnub.4.12.0.js
// @require     https://cdnjs.cloudflare.com/ajax/libs/notify/0.4.2/notify.js
// @grant       GM_addStyle
// ==/UserScript==
/*- The @grant directive is needed to work around a design change
    introduced in GM 1.0.   It restores the sandbox.
*/
//--- Use jQuery to add the form in a "popup" dialog.


console.log("Subscribe User Notification");

    pubnub = new PubNub({
        publishKey : 'pub-c-24317e97-b457-4711-8b10-6dd17aaefa3b',
        subscribeKey : 'sub-c-8696da1e-69ab-11e7-8127-0619f8945a4f'
    })
console.log("Connected");
    pubnub.addListener({
        status: function(s) {
        // handle status
        var category = s.category; //PNConnectedCategory
        var operation = s.operation; //PNSubscribeOperation
        var affectedChannels = s.affectedChannels; //The channels affected in the operation, of type array.
        var subscribedChannels = s.subscribedChannels; //All the current subscribed channels, of type array.
        var affectedChannelGroups = s.affectedChannelGroups; //The channel groups affected in the operation, of type array.
        var lastTimetoken = s.lastTimetoken; //The last timetoken used in the subscribe request, of type long.
        var currentTimetoken = s.currentTimetoken; //The current timetoken fetched in the subscribe response, which is going to be used in the next request, of type long.
    },
        message: function(message) {
            var newMsg = message;
            console.log("New Message! ", newMsg.message);
            $.notify(newMsg.message.text);
        },
        presence: function(presenceEvent) {
            // handle presence
        }
    })
    console.log("Subscribing user channel");
    pubnub.subscribe({
        channels: ['userChannel']
    });
