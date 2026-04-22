# Analytics Dashboard - Quick Start

## 🎯 Get Running in 5 Steps

### Step 1: Start Backend (Terminal 1)
```bash
cd c:\Users\LENOVO\Desktop\ooad_jackfruit\analyticsDashboard
mvn spring-boot:run
```
**Wait for:** `Tomcat started on port(s): 8080`

---

### Step 2: Verify API (Terminal 2)
```bash
Invoke-RestMethod -Uri "http://localhost:8080/api/dashboard" -Method GET
```
**Expected:** JSON response with KPIs, alerts, insights

---

### Step 3: Install React (Terminal 2)
```bash
cd c:\Users\LENOVO\Desktop\ooad_jackfruit\analyticsDashboard\ui
npm install
```

---

### Step 4: Start React (Terminal 2)
```bash
npm start
```
**Browser opens:** `http://localhost:3000`

---

### Step 5: View Dashboard
Open browser → see beautiful analytics dashboard! 🎉

---

## 🔄 Running Again Later

**Terminal 1:**
```bash
cd c:\Users\LENOVO\Desktop\ooad_jackfruit\analyticsDashboard
mvn spring-boot:run
```

**Terminal 2:**
```bash
cd c:\Users\LENOVO\Desktop\ooad_jackfruit\analyticsDashboard\ui
npm start
```

---

## ❌ Troubleshooting

| Problem | Solution |
|---------|----------|
| Port 8080 in use | `mvn spring-boot:run -Dserver.port=8081` |
| Can't connect to API | Check backend is running on 8080 |
| npm install fails | `npm cache clean --force && npm install` |
| React won't start | Delete `node_modules/` and run `npm install` again |

---

## 📖 Full Documentation
See: `RUN_GUIDE.md`

---

**Status:** ✅ Ready to Run!
