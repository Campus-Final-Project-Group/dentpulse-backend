import os
import requests
from dotenv import load_dotenv

load_dotenv()

SMSAPI_TOKEN = os.getenv("SMSAPI_TOKEN")
SMSAPI_SENDER = os.getenv("SMSAPI_SENDER")

SMSAPI_URL = "https://dashboard.smsapi.lk/api/v3/sms/send"


def send_sms_from_smsapi(recipient, message, schedule_time=None):

    headers = {
        "Authorization": f"Bearer {SMSAPI_TOKEN}",
        "Content-Type": "application/json",
        "Accept": "application/json"
    }

    payload = {
        "recipient": recipient,
        "sender_id": SMSAPI_SENDER,
        "type": "plain",
        "message": message
    }
    
    print("TOKEN:", SMSAPI_TOKEN)
    print("SENDER:", SMSAPI_SENDER)
    if schedule_time:
        payload["schedule_time"] = schedule_time

    response = requests.post(SMSAPI_URL, json=payload, headers=headers)

    if response.status_code == 200:
        print("SMS sent successfully:", response.json())
        return response.json()
    else:
        print("SMS failed:", response.text)
        return None
