import sys
import json
import PySimpleGUI as sg
from core.excel_operator import ExcelOperator
from core.ai_handler import AIHandler
from configparser import ConfigParser

class ExcelAssistant:
    def __init__(self):
        self.excel = ExcelOperator()
        self.ai = AIHandler()
        self.config = ConfigParser()
        self.config.read('config/config.ini')
        
    def run(self):
        layout = [
            [sg.Text("请输入您的操作指令:")],
            [sg.Multiline(size=(60,5), key='-INPUT-')],
            [sg.Button('执行'), sg.Button('保存'), sg.Button('退出')],
            [sg.Output(size=(60,20))]
        ]
        
        window = sg.Window('Excel智能助手', layout)
        
        while True:
            event, values = window.read()
            if event in (sg.WIN_CLOSED, '退出'):
                break
                
            if event == '执行':
                self.process_instruction(values['-INPUT-'])
                
            if event == '保存':
                self.excel.save_workbook()
                
        window.close()
        self.excel.close()
        
    def process_instruction(self, instruction):
        try:
            operations = self.ai.parse_instruction(instruction)
            if not operations:
                print("无法解析指令")
                return
                
            for op in operations.get('operations', []):
                self.execute_operation(op)
                
            print("操作执行完成")
            
        except Exception as e:
            print(f"执行出错: {str(e)}")
            
    def execute_operation(self, operation):
        op_type = operation.get('type')
        params = operation.get('parameters', {})
        
        try:
            if op_type == 'filter':
                self.excel.apply_filter(params.get('column'), params.get('condition'))
            elif op_type == 'formula':
                self.excel.insert_formula(params.get('cell'), params.get('expression'))
            elif op_type == 'chart':
                self.excel.create_chart(
                    params.get('type'),
                    params.get('data_range'),
                    params.get('position', 'A1')
                )
            elif op_type == 'clean':
                self.excel.clean_data(
                    params.get('action'),
                    params.get('target')
                )
            else:
                print(f"未知操作类型: {op_type}")
        except Exception as e:
            print(f"操作执行失败: {str(e)}")

if __name__ == '__main__':
    if not sys.platform.startswith('win'):
        sg.popup_error("该程序只能在Windows环境下运行")
        sys.exit()
        
    assistant = ExcelAssistant()
    assistant.run()
