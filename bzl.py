from openpyxl import load_workbook
from openpyxl.styles import Font, Alignment
from openpyxl.utils import get_column_letter

# 定义一个函数来处理特定的行
def process_line(ws, fruits, last_column, font, alignment):
    # 遍历每个水果
    for fruit in fruits:
        # 检查水果名称的前两个字符是否在Excel的第一列中
        for i, row in enumerate(ws.iter_rows(min_row=2, max_col=1, values_only=True), start=2):
            if fruit[:2] in row:
                # 如果找到了，就写在那一行的最后一列，并设置字体和对齐方式
                cell = ws.cell(row=i, column=last_column, value=fruit)
                cell.font = font
                cell.alignment = alignment
                break
        else:
            # 如果没找到，就把那个水果的前两个字写到第一列最后一个位置，再把他写到最后一列的那个位置，并设置字体和对齐方式
            ws.append({1: fruit[:2], last_column: fruit})
            new_row = ws[ws.max_row]
            for cell in new_row:
                cell.font = font
                cell.alignment = alignment

# 读取Excel文件
wb = load_workbook('fruits.xlsx')
ws = wb.active

# 创建一个Font对象，设置字体为微软雅黑，字号为8
font = Font(name='微软雅黑', size=8)

# 创建一个Alignment对象，设置文本居中显示和自动换行
alignment = Alignment(horizontal='center', vertical='center', wrap_text=True)

# 读取文本文件，使用utf-8编码
with open('fruits.txt', 'r', encoding='utf-8') as file:
    lines = file.readlines()

# 过滤掉空行
lines = [line for line in lines if line.strip() != '']

# 获取Excel文件的最后一列
last_column = ws.max_column + 1

# 如果文件只有一行，将这一行视为三行模式下的第二行进行处理
if len(lines) == 1:
    # 对这一行进行split，并调用process_line函数处理
    fruits = lines[0].split()
    process_line(ws, fruits, last_column, font, alignment)

# 如果文件有三行或者五行
elif len(lines) >= 3:
    # 处理前三行
    cell1 = ws.cell(row=3, column=last_column, value=lines[0].strip())
    cell1.font = font
    cell1.alignment = alignment

    cell2 = ws.cell(row=4, column=last_column, value=lines[2].strip())
    cell2.font = font
    cell2.alignment = alignment

    # 对第二行进行split，并调用process_line函数处理
    fruits = lines[1].split()
    process_line(ws, fruits, last_column, font, alignment)

    if len(lines) >= 5:
        # 处理第四行和第五行
        last_column += 1

        cell3 = ws.cell(row=3, column=last_column, value=lines[3].strip())
        cell3.font = font
        cell3.alignment = alignment

        # 对第五行进行split，并调用process_line函数处理
        fruits = lines[4].split()
        process_line(ws, fruits, last_column, font, alignment)

# 设置所有列的宽度为20，所有行的高度为10
for i in range(1, ws.max_column + 1):
    ws.column_dimensions[get_column_letter(i)].width = 10

for row in ws.iter_rows():
    ws.row_dimensions[row[0].row].height = 13

# 保存更改后的Excel文件
wb.save('fruits.xlsx')
