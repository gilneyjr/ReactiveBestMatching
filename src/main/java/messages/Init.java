package messages;

import java.nio.file.Path;

public class Init {
	private Path dic;
	private Path input;
	public Init(Path dic, Path input) {
		super();
		this.dic = dic;
		this.input = input;
	}
	public Path getDic() {
		return dic;
	}
	public Path getInput() {
		return input;
	}
}
