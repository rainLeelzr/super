@echo OFF
echo ��Э�鶨��ô����ProtoFile�ļ�����

echo �������ɡ�IM��proto
protoc --java_out=../java ./ProtoFile/IM.proto

echo �������ɡ�BaseFrame��proto
protoc --java_out=../java ./ProtoFile/BaseFrame.proto

echo �ļ�������
pause