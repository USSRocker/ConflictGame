class CellInfo {
	int dimensionsCount;
	public double[] Profit;
	public CellInfo(int dimensions)
	{
		this.dimensionsCount = dimensions;
		Profit = new double[dimensionsCount];
	}
	/*public CellInfo(double[] profit)
	{
		this.Profit = profit.clone();
		dimensionsCount = this.Profit.length;
	}*/
}

public class MultiArray {
	int dimensionsCount, volume;
	int[] dimensionLengths;
	CellInfo[] cells;
	int[] shifts;
	public MultiArray(int[] dimensions) {
		for(int i = 0; i < dimensions.length; i++)
			if(dimensions[i] < 1)
				try {
					throw new Exception("A dimension can not have a length less than one!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		dimensionLengths = dimensions.clone();
		dimensionsCount = dimensionLengths.length;
		shifts = new int[dimensionsCount];
		shifts[0] = 1;
		for(int i = 1; i < dimensionsCount; i++)
			shifts[i] = shifts[i - 1] * dimensionLengths[i - 1];
		volume = shifts[dimensionsCount - 1] * dimensionLengths[dimensionsCount - 1];
		cells = new CellInfo[volume];
	}
	public CellInfo At(int[] coordinate) throws Exception
	{
		int index = GetInternalIndex(coordinate);
		if(cells[index] == null)
			cells[index] = new CellInfo(dimensionsCount); 
		return cells[index];
	}
	public boolean Existing(int[] coordinate) throws Exception
	{
		return cells[GetInternalIndex(coordinate)] == null ? false : true;
	}
	private int GetInternalIndex(int[] coordinate) throws Exception
	{
		if(coordinate.length != dimensionsCount)
			throw new Exception("Wrong coordinate: not equal count of dimensions!");

		int index = 0;
		for(int i = 0; i < dimensionsCount; i++)
		{
			if(coordinate[i] < 0)
				throw new Exception("Wrong coordinate: coordinate has negative element!");
			if(coordinate[i] >= dimensionLengths[i])
				throw new Exception("Wrong coordinate: coordinate's element is out of a dimension!");
			index += shifts[i] * coordinate[i];
		}
		return index;
	}
	/*public void Set(int[] coordinate, CellInfo info) throws Exception
	{
		cells[GetInternalIndex(coordinate)] = info;
	}*/
}
