from openai import OpenAI
import json
from configparser import ConfigParser

class AIHandler:
    def __init__(self):
        config = ConfigParser()
        config.read('config/config.ini')
        self.client = OpenAI(api_key=config.get('openai', 'api_key'))
        self.system_prompt = """
        你是一个Excel操作专家，需要将用户的自然语言指令转换为具体的操作步骤。请严格按照以下JSON格式响应：
        
        {
            "operations": [
                {
                    "type": "filter|formula|chart|clean",
                    "parameters": {
                        // 根据操作类型不同参数结构不同
                    }
                }
            ]
        }
        
        支持的操作类型：
        1. filter - 数据筛选: {"column": 列号(int), "condition": "筛选条件字符串"}
        2. formula - 插入公式: {"cell": "单元格地址", "expression": "Excel公式"}
        3. chart - 创建图表: {"type": "柱状图|折线图|饼图", "data_range": "数据范围"}
        4. clean - 数据清洗: {"action": "去重|填充缺失值", "target": "列号或'all'"}
        """
        
    def parse_instruction(self, user_input):
        try:
            response = self.client.chat.completions.create(
                model="gpt-4-1106-preview",
                messages=[
                    {"role": "system", "content": self.system_prompt},
                    {"role": "user", "content": user_input}
                ],
                response_format={"type": "json_object"}
            )
            return json.loads(response.choices[0].message.content)
        except Exception as e:
            print(f"AI处理失败: {str(e)}")
            return None
