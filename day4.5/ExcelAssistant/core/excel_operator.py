import win32com.client
import pythoncom

class ExcelOperator:
    def __init__(self):
        pythoncom.CoInitialize()
        self.xl = win32com.client.Dispatch("Excel.Application")
        self.xl.Visible = True
        self.wb = self.xl.ActiveWorkbook
        
    def get_active_sheet_data(self):
        """获取当前活动工作表的数据"""
        sheet = self.wb.ActiveSheet
        return sheet.UsedRange.Value
    
    def apply_filter(self, column, condition):
        """应用自动筛选"""
        sheet = self.wb.ActiveSheet
        sheet.UsedRange.AutoFilter(Field=column, Criteria1=condition)
        
    def insert_formula(self, cell_address, formula):
        """插入公式到指定单元格"""
        sheet = self.wb.ActiveSheet
        sheet.Range(cell_address).Formula = formula
        
    def create_chart(self, chart_type, data_range, position):
        """创建图表"""
        sheet = self.wb.ActiveSheet
        chart = sheet.Shapes.AddChart2().Chart
        chart.ChartType = {
            "柱状图": 51,   # xlColumnClustered
            "折线图": 4,    # xlLine
            "饼图": 5       # xlPie
        }.get(chart_type, 51)
        chart.SetSourceData(sheet.Range(data_range))
        chart.Location(2, sheet.Range(position))  # xlLocationAsObject
        
    def save_workbook(self, filepath=None):
        """保存工作簿"""
        if filepath:
            self.wb.SaveAs(filepath)
        else:
            self.wb.Save()
            
    def clean_data(self, action, target_column=None):
        """数据清洗"""
        sheet = self.wb.ActiveSheet
        used_range = sheet.UsedRange
        
        if action == "去重":
            columns = [used_range.Columns(i) for i in range(1, used_range.Columns.Count+1)]
            if target_column and isinstance(target_column, int):
                used_range.RemoveDuplicates(columns[target_column-1])
            else:
                used_range.RemoveDuplicates(columns)
        elif action == "填充缺失值":
            if target_column and isinstance(target_column, int):
                col = used_range.Columns(target_column)
                for cell in col:
                    if cell.Value is None:
                        cell.Value = 0
            else:
                for row in used_range.Rows:
                    for cell in row.Cells:
                        if cell.Value is None:
                            cell.Value = 0
        
    def close(self):
        """关闭Excel实例"""
        self.wb.Close(False)
        self.xl.Quit()
        pythoncom.CoUninitialize()
