from apscheduler.schedulers.blocking import BlockingScheduler
from reminder_service import send_reminders
from datetime import datetime

scheduler = BlockingScheduler()

# Run every day at 9 AM
@scheduler.scheduled_job('cron', hour=9, minute=0)
def daily_reminder_job():
    print("🔔 Scheduler started at:", datetime.now())
    send_reminders()

if __name__ == "__main__":
    print("🚀 Reminder Scheduler Running...")
    scheduler.start()
