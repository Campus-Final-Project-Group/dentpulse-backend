import mysql.connector
from config import DB_CONFIG
from datetime import datetime
def get_connection():
    return mysql.connector.connect(**DB_CONFIG)

def get_customers_for_reminder():
    sql_query="""SELECT 
    p.email,
    p.full_name,
    p.phone,
    a.appointment_date,
    a.start_time,
    a.treatment_type
FROM appointment a
INNER JOIN patient p 
    ON a.patient_id = p.patient_id;
"""
    conn=get_connection()
    cursor=conn.cursor()
    cursor.execute(sql_query)
    result=cursor.fetchall()
    cursor.close()
    conn.close()
    return result

def save_reminder_log(name, phone, email, reminder_type, appointment_date):

    conn = get_connection()
    cursor = conn.cursor()

    sql = """
    INSERT INTO reminder_log 
    (customer_name, phone_number, email, reminder_type, appointment_date,sent_at)
    VALUES (%s, %s, %s, %s, %s,%s)
    """

    cursor.execute(sql, (name, phone, email, reminder_type, appointment_date,datetime.now()))

    conn.commit()

    cursor.close()
    conn.close()            
    
    
