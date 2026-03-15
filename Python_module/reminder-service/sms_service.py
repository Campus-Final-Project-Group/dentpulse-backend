import os
from dotenv import load_dotenv
from twilio.rest import Client


TWILIO_PHONE=os.getenv("TWILIO_PHONE_NUMBER")
TWILIO_ACCOUNT_SID=os.getenv("TWILIO_ACCOUNT_SID")
TWILIO_AUTH_TOKEN=os.getenv("TWILIO_AUTH_TOKEN")

client = Client(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN)


def send_sms(to_number, message):

    message = client.messages.create(
        body=message,
        from_=TWILIO_PHONE,
        to=to_number
    )

    print("SMS sent:", message.sid)
    
