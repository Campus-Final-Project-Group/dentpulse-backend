from fastapi import FastAPI
from db import get_customers_for_reminder,save_reminder_log
from email_service import send_email
from apscheduler.schedulers.background import BackgroundScheduler
from datetime import datetime, timedelta, time as dt_time
from sms_service import send_sms
from smsapi_service import send_sms_from_smsapi

app = FastAPI()

    
scheduler = BackgroundScheduler()

def send_single_reminder(email, phone,name, date, time, treatment):

   
        subject = "Appointment Reminder"
        body = f"""
<!DOCTYPE html>
<html>
<body style="margin:0; padding:0; font-family: Arial, sans-serif; background-color:#f0fdf4;">

  <div style="max-width:600px; margin:auto; background:white; border-radius:10px; overflow:hidden; box-shadow:0 4px 10px rgba(0,0,0,0.05);">
    
    <!-- Header -->
    <div style="background:#16a34a; padding:20px; text-align:center; color:white;">
      <h2 style="margin:0;">DentPulse Dental Clinic</h2>
      <p style="margin:5px 0 0;">Appointment Reminder</p>
    </div>

    <!-- Content -->
    <div style="padding:25px; color:#374151;">
      
      <p style="font-size:16px;">Hello <strong>{name}</strong>,</p>

      <p>This is a friendly reminder for your upcoming appointment.</p>

      <div style="background:#f0fdf4; padding:15px; border-radius:8px; margin:20px 0;">
        <p><strong>Date:</strong> {date}</p>
        <p><strong>Time:</strong> {time}</p>
        <p><strong>Treatment:</strong> {treatment}</p>
      </div>

      <p style="margin-top:20px;">
        Please arrive 10 minutes early.
      </p>

      <div style="margin-top:30px; text-align:center;">
        <a href="http://yourwebsite.com"
           style="background:#16a34a; color:white; padding:12px 25px; text-decoration:none; border-radius:6px;">
           View Appointment
        </a>
      </div>

    </div>

    <!-- Footer -->
    <div style="background:#f9fafb; padding:15px; text-align:center; font-size:12px; color:#6b7280;">
      © 2025 DentPulse Dental Clinic <br>
      Thank you for trusting us with your smile 🦷
    </div>

  </div>

</body>
</html>
"""
    

        send_email(email, subject, body)
        save_reminder_log(
        name=name,
        phone=phone,
        email=email,
        reminder_type="EMAIL",
        appointment_date=date
        
    )
        print("reminder procesed for:", email)
def format_contact_for_smsapi(number):

    number = number.replace(" ", "")

    if number.startswith("0"):
        return "94" + number[1:]
    elif number.startswith("+94"):
        return number[1:]
    elif number.startswith("94"):
        return number
    else:
        raise ValueError("Invalid Sri Lankan phone number")


def send_sms_reminder_from_smsapi(email,phone,name, date, time, treatment):

    formatted_phone = format_contact_for_smsapi(phone)

    message = f"""
DentPulse Reminder

Hello {name},
Appointment: {date} at {time}
Treatment: {treatment}

Please arrive 10 minutes early.
"""

    #send_sms_from_smsapi(formatted_phone, message)
    # save_reminder_log(
    #     name=name,
    #     phone=phone,
    #     email=email,
    #     reminder_type="SMS",
    #     appointment_date=date
       
    # )
    # print("SMSAPI reminder sent to:", phone)

    
def format_contact(number):
    
    if number.startswith("0"):
        return "+94" + number[1:]
    elif number.startswith("+94"):
        return number
    else:
        raise ValueError("Invalid phone number format")
  
def send_sms_reminder(phone,name, date, time, treatment):
  
        message = f"""Hello {name}, This is a friendly reminder for your upcoming appointment. Date: {date}
        Time: {time}
        Treatment: {treatment} Please arrive 10 minutes early. Thank you, DentPulse Dental Clinic"""
        #send_sms(format_contact(phone), message)
        print("SMS reminder sent to:", phone)
    

def schedule_reminders():
    customers = get_customers_for_reminder()

    for email,name,phone,date, db_time, treatment in customers:

        if isinstance(db_time, timedelta):
            total_seconds = int(db_time.total_seconds())
            hours = total_seconds // 3600
            minutes = (total_seconds % 3600) // 60
            seconds = total_seconds % 60
            db_time = dt_time(hour=hours, minute=minutes, second=seconds)

        appointment_datetime = datetime.combine(date, db_time)

       
        reminder_time = appointment_datetime - timedelta(days=1)

      
        if reminder_time > datetime.now():
            scheduler.add_job(
                send_single_reminder,
                trigger="date",
                run_date=reminder_time,
                args=[email,phone ,name, date, db_time, treatment],
                id=f"email_{email}_{date}",
                replace_existing=True
                
            )
            scheduler.add_job(
                send_sms_reminder_from_smsapi,
                trigger="date",
                run_date=reminder_time,
                args=[email,phone, name, date, db_time, treatment],
                id=f"sms_{phone}_{date}",
                replace_existing=True
            )
            print(f"Reminder scheduled for {email} at {reminder_time}")
            



@app.post("/send-reminders")
def send_reminders():
    schedule_reminders()
    return {"status": "success", "message": "Reminders sent"}
 
# @app.post("/test-sms")
# def test_sms():
#     send_sms_from_smsapi(
#         "94714930320",   
#         "Hello from DentPulse test"
#     )
#     return {"status": "SMS triggered"}
     
@app.on_event("startup")
def start_scheduler():
    scheduler.start()
    schedule_reminders()
    print("Scheduler started at application startup")
    
    

@app.post("/test-email")
def test_email():
    send_single_reminder(
        email="sonicorzone@gmail.com",
        phone="0712345678",
        name="Test User",
        date=datetime.now().date(),
        time="10:00 AM",
        treatment="Dental Checkup"
    )

    return {"status": "success", "message": "Test email sent"}