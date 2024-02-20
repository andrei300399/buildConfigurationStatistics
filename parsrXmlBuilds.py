import xml.etree.ElementTree as ET
from datetime import datetime
import dateutil.relativedelta
import re
from random import randrange

curr_dt = datetime.now()

len_builds = 25

path_to_builds = "C:\\buildConfigurationStatistics\\work\\jobs\\test-Build-frontend-maven-plugin_version\\builds\\"

name_file_build = "build.xml"
months_rel = 0

for i in range(0, len_builds):
    root = ET.parse(f"{path_to_builds}{i + 1}\\{name_file_build}")
    print(f"months_rel: {months_rel}")

    if i == 4:
        continue

    print("Current datetime: ", curr_dt)
    timestamp = int(round(curr_dt.timestamp()))*1000

    print("Integer timestamp of current datetime: ", timestamp)

    new_date = curr_dt - dateutil.relativedelta.relativedelta(months=months_rel)

    print("new_date: ", new_date)
    timestamp_new = int(round(new_date.timestamp()))*1000

    print("timestamp_new: ", timestamp_new)

    new_root = root.getroot()
    print(root)
    timestampText = new_root.find('timestamp').text
    print(timestampText)
    root.find('timestamp').text = str(timestamp_new)

    startTimeText = new_root.find('startTime').text
    print(startTimeText)
    rand_milli = randrange(5, 25)
    root.find('startTime').text = str(timestamp_new + rand_milli)
    print(rand_milli)

    root.write(f'{path_to_builds}{i + 1}\\{name_file_build}', encoding = "UTF-8", xml_declaration = True)

    if i % 2 == 1 and i < 4:
        months_rel += 1

    if i % 2 == 0 and i > 4:
        months_rel += 1

    # rewrire version xml

    with open(f"{path_to_builds}{i + 1}\\{name_file_build}", "r") as rfile:
        s = rfile.read()
        rplce = re.sub('version=\'1.0\'', "version=\'1.1\'", s)
    with open(f"{path_to_builds}{i + 1}\\{name_file_build}", "w") as wfile:
        wfile.write(rplce)

### 1707287342000