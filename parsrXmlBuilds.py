import xml.etree.ElementTree as ET
from datetime import datetime
import dateutil.relativedelta
import re

curr_dt = datetime.now()

len_builds = 18

for i in range(0, len_builds):
    root = ET.parse(f"C:\\buildConfigurationStatistics\\work\\jobs\\test-Build-frontend-maven-plugin\\builds\\{i + 1}\\build.xml")

    print("Current datetime: ", curr_dt)
    timestamp = int(round(curr_dt.timestamp()))*1000

    print("Integer timestamp of current datetime: ", timestamp)

    new_date = curr_dt - dateutil.relativedelta.relativedelta(months=i)

    print("new_date: ", new_date)
    timestamp_new = int(round(new_date.timestamp()))*1000

    print("timestamp_new: ", timestamp_new)

    new_root = root.getroot()
    print(root)
    timestampText = new_root.find('timestamp').text
    print(timestampText)
    root.find('timestamp').text = str(timestamp_new)

    root.write(f'C:\\buildConfigurationStatistics\\work\\jobs\\test-Build-frontend-maven-plugin\\builds\\{i + 1}\\build_new.xml', encoding = "UTF-8", xml_declaration = True)

    # rewrire version xml

    with open(f"C:\\buildConfigurationStatistics\\work\\jobs\\test-Build-frontend-maven-plugin\\builds\\{i + 1}\\build_new.xml", "r") as rfile:
        s = rfile.read()
        rplce = re.sub('version=\'1.0\'', "version=\'1.1\'", s)
    with open(f"C:\\buildConfigurationStatistics\\work\\jobs\\test-Build-frontend-maven-plugin\\builds\\{i + 1}\\build_new.xml", "w") as wfile:
        wfile.write(rplce)

### 1706547093301