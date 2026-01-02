from flask import Flask, request, jsonify
import pandas as pd
import pickle

app = Flask(__name__)

# Load trained model
with open("xgb_model.pkl", "rb") as f:
    model = pickle.load(f)

@app.route("/predict", methods=["POST"])
def predict():
    data = request.json

    df = pd.DataFrame([{
        "Weekday_or_Weekend": data["weekdayOrWeekend"],
        "Month": data["month"],
        "Day_of_Week": data["dayOfWeek"],
        "Hour": data["hour"],
        "Past_Appointments": data["pastAppointments"]
    }])

    pred = model.predict(df)[0]
    prob = model.predict_proba(df)[0][1]

    return jsonify({
        "busyLabel": "High Busy" if pred == 1 else "Low Busy",
        "probability": round(float(prob), 2)
    })

if __name__ == "__main__":
    app.run(port=5000, debug=True)
